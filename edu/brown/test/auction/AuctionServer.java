package brown.test.auction;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Account;
import brown.auctions.BidBundle;
import brown.auctions.mechanisms.OutcryAuction;
import brown.auctions.mechanisms.SealedBid;
import brown.messages.Registration;
import brown.server.AgentServer;
import brown.setup.Logging;

import com.esotericsoftware.kryonet.Connection;

public class AuctionServer extends AgentServer {
	public double MAX = 0;

	public AuctionServer(int port) {
		super(port, new GameSetup());
	}
	
	@Override
	protected void onRegistration(Connection connection, Registration registration) {
		Integer theID = this.defaultRegistration(connection, registration);
		if (theID == null) {
		  return;
		}
		
		double nextValue = Math.random()*100;
		this.MAX = Math.max(nextValue, this.MAX);
		this.theServer.sendToTCP(connection.getID(), 
		    new AuctionRegistration(theID, nextValue));
		
		Account oldAccount = bank.get(connections.get(connection));
		Account newAccount = oldAccount.addAll(100, null);
		bank.put(connections.get(connection), newAccount);
		
		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
	}
	
	public double runGame(boolean outcry, boolean firstprice) {
		if (outcry) {
			this.auctions.put(0, new OutcryAuction(0, new TheGood(), false, true, firstprice));
		} else {
			this.auctions.put(0, new SealedBid(0, new TheGood(), true, firstprice));
		}
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
		return bundle.getCost();
	}
	
	public static void main(String[] args) {
		AuctionServer server = new AuctionServer(2121);
		server.runGame(false, true);
	}

}
