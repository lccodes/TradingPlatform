package brown.server.library;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.rules.allocationrules.OpenOutcryRule;
import brown.assets.accounting.Account;
import brown.bundles.BidBundle;
import brown.bundles.BundleType;
import brown.bundles.SimpleBidBundle;
import brown.messages.Registration;
import brown.rules.paymentrules.library.FirstPriceRule;
import brown.registrations.AuctionRegistration;
import brown.server.AgentServer;
import brown.setup.Logging;
import brown.setup.library.LabGameSetup;
import brown.tradeables.Tradeable;

import com.esotericsoftware.kryonet.Connection;

public class AuctionServer extends AgentServer {
	public double MAX = 0;

	public AuctionServer(int port) {
		super(port, new LabGameSetup());
	}

	@Override
	protected void onRegistration(Connection connection,
			Registration registration) {
		Integer theID = this.defaultRegistration(connection, registration);
		if (theID == null) {
			return;
		}

		double nextValue = Math.random() * 100;
		this.MAX = Math.max(nextValue, this.MAX);
		this.theServer.sendToTCP(connection.getID(), new AuctionRegistration(
				theID, nextValue));

		Account oldAccount = acctManager.getAccount(connections.get(connection));
		Account newAccount = oldAccount.addAll(100, null);
		acctManager.setAccount(connections.get(connection), newAccount);

		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
	}

	public double runGame(boolean outcry, boolean firstprice) {
		Set<Tradeable> theSet = new HashSet<Tradeable>();
		theSet.add(new TheGood());
		// if (outcry) {
		// this.auctions.put(0, new OutcryAuction(0, new TheGood(), false, true,
		// firstprice));
		this.manager.open(new OneSidedAuction(0, theSet, new OpenOutcryRule(
				BundleType.Simple, true, 5, new SimpleBidBundle(10, null,
						BundleType.Simple)), new FirstPriceRule()));
		// } else {
		// this.auctions.put(0, new SealedBid(0, new TheGood(), true,
		// firstprice));
		// }
		int i = 0;
		while (i < 2) {
			try {
				Thread.sleep(2000);
				Logging.log("[-] setup phase " + i++);
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}

		while (!this.manager.getOneSided(0).isClosed()) {
			try {
				Thread.sleep(2000);
				this.updateAllAuctions();
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}
		Map<BidBundle, Set<Tradeable>> bundles = this.manager.getOneSided(0)
				.getWinners();
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
