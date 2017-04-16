package brown.lab.lab3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.accounting.Account;
import brown.assets.value.FullType;
import brown.assets.value.ITradeable;
import brown.assets.value.TradeableType;
import brown.auctions.activity.SimpleNoJumpActivityRule;
import brown.auctions.allocation.SimpleDemandAllocation;
import brown.auctions.allocation.SimpleHighestBidderAllocation;
import brown.auctions.info.AnonymousPolicy;
import brown.auctions.interfaces.Market;
import brown.auctions.payment.SimpleSecondPrice;
import brown.auctions.payment.SimpleSecondPriceDemand;
import brown.auctions.query.OutcryQueryRule;
import brown.auctions.query.SealedBidQuery;
import brown.auctions.state.SimpleInternalState;
import brown.auctions.termination.OneShotTermination;
import brown.auctions.termination.SamePaymentsTermination;
import brown.lab.GameSetup;
import brown.lab.ValuationRegistration;
import brown.messages.Registration;
import brown.server.AgentServer;
import brown.setup.Logging;

import com.esotericsoftware.kryonet.Connection;

/**
 * Implementation of lab3 server.
 * 
 * @author lcamery
 */
public class Lab3Server extends AgentServer {
	private final Set<Integer> INTS;

	public Lab3Server(int port) {
		super(port, new GameSetup());
		this.INTS = new HashSet<Integer>();
		for (int i = 0; i < 97; i++) {
			this.INTS.add(i);
		}
	}

	@Override
	protected void onRegistration(Connection connection,
			Registration registration) {
		Integer theID = this.defaultRegistration(connection, registration);
		if (theID == null) {
			return;
		}

		Map<FullType,Double> value = new HashMap<FullType,Double>();
		for (Integer i : this.INTS) {
			if (Math.random() < .1) {
				continue;
			}
			double nextValue = Math.random() * 50;
			value.put(new FullType(TradeableType.Custom,i), nextValue);
		}
		this.theServer.sendToTCP(connection.getID(), new ValuationRegistration(
				theID, value));

		Account oldAccount = bank.get(connections.get(connection));
		Account newAccount = oldAccount.addAll(1000, null);
		bank.put(connections.get(connection), newAccount);

		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
	}

	public void runGame(boolean outcry, boolean firstprice, double reserve) {
		// Constructs auction according to rules
		Set<ITradeable> theSet = new HashSet<ITradeable>();
		for(Integer ID : this.INTS) {
			theSet.add(new Lab3Good(ID));
		}
		//PaymentRule prule = firstprice ? new FirstPriceRule()
		//		: new SecondPriceRule();
		if (outcry) {
			this.manager.open(new Market(new SimpleSecondPriceDemand(), new SimpleDemandAllocation(),
					new OutcryQueryRule(), new AnonymousPolicy(),
					new SamePaymentsTermination(3), new SimpleNoJumpActivityRule(),
					new SimpleInternalState(0, theSet)));
		} else {
			this.manager.open(new Market(new SimpleSecondPrice(), new SimpleHighestBidderAllocation(),
					new SealedBidQuery(), new AnonymousPolicy(),
					new OneShotTermination(), new SimpleNoJumpActivityRule(),
					new SimpleInternalState(0, theSet)));
		}

		// Gives everyone 20 seconds to join the auction
		int i = 0;
		while (i < 3) {
			try {
				Thread.sleep(1000);
				Logging.log("[-] setup phase " + i++);
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}

		// Runs the auction to completion
		//Market market = this.manager.getIMarket(0);
		while (this.manager.getAuctions().size() > 0) {
			try {
				Thread.sleep(1000);
				this.updateAllAuctions();
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}

		// Logs the winner and price
		//Map<BidBundle, Set<ITradeable>> bundles = this.manager.getOneSided(0)
		//		.getWinners();
		//Logging.log("[-] auction over");
		//for (BidBundle b : bundles.keySet()) {
		//	Logging.log("[-] winner: " + b.getAgent() + " for " + b.getCost());
		//}
		for (Account account : this.bank.values()) {
			System.out.println(account);
		}
	}

	public static void main(String[] args) {
		Lab3Server l3s = new Lab3Server(2121);
		l3s.runGame(false, true, 0);
	}

}
