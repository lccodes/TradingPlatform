package brown.test.auction;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.accounting.Account;
import brown.assets.value.Tradeable;
import brown.auctions.OneSidedAuction;
import brown.auctions.arules.OpenOutcryRule;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.prules.FirstPriceRule;
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
		Set<Tradeable> theSet = new HashSet<Tradeable>();
		theSet.add(new TheGood());
		//if (outcry) {
			//this.auctions.put(0, new OutcryAuction(0, new TheGood(), false, true, firstprice));
			this.auctions.put(0, new OneSidedAuction(0, theSet, 
					new OpenOutcryRule(BundleType.SimpleOutcry, true, 5,
							new SimpleBidBundle(10,null,BundleType.SimpleOutcry)),
					new FirstPriceRule()));
		//} else {
			//this.auctions.put(0, new SealedBid(0, new TheGood(), true, firstprice));
		//}
		int i = 0;
		while (i < 10) {
			try {
				Thread.sleep(2000);
				Logging.log("[-] setup phase " + i++);
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}
		
		while(!this.auctions.get(0).isClosed()) {
			try {
				Thread.sleep(2000);
				this.updateAllAuctions();
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}
		Map<BidBundle, Set<Tradeable>> bundles = this.auctions.get(0).getWinners();
		Logging.log("[-] auction over");
		for (BidBundle b : bundles.keySet()) {
			Logging.log("[-] winner: " + b.getAgent() + " for " + b.getCost());
			return b.getCost();
		}
		
		return -1;
	}
	
	public static void main(String[] args) {
		AuctionServer server = new AuctionServer(2121);
		server.runGame(false, true);
	}

}
