package brown.test.auction;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Account;
import brown.auctions.BidBundle;
import brown.auctions.mechanisms.EnglishAuction;
import brown.messages.Registration;
import brown.server.AgentServer;
import brown.setup.Logging;

import com.esotericsoftware.kryonet.Connection;

public class AuctionServer extends AgentServer {

	public AuctionServer(int port) {
		super(port);
		this.theServer.getKryo().register(TheGood.class);
	}
	
	@Override
	protected void onRegistration(Connection connection, Registration registration) {
		super.onRegistration(connection, registration);
		
		Account oldAccount = bank.get(connections.get(connection));
		Account newAccount = oldAccount.addAll(100, null);
		bank.put(connections.get(connection), newAccount);
		
		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
	}
	
	public void runGame() {
		this.auctions.put(0, new EnglishAuction(0, new TheGood(), true));
		int i = 0;
		while (i < 10) {
			try {
				Thread.sleep(2000);
				Logging.log("[-] setup phase " + i++);
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}
		
		while(!this.auctions.get(0).isOver()) {
			try {
				Thread.sleep(2000);
				this.updateAllAuctions();
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}
		BidBundle bundle = this.auctions.get(0).getWinner();
		Logging.log("[-] auction over");
		Logging.log("[-] winner: " + bundle.getAgent() + " for " + bundle.getCost());
	}
	
	public static void main(String[] args) {
		AuctionServer server = new AuctionServer(9898);
		server.runGame();
	}

}
