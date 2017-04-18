package brown.lab.lab8;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import brown.assets.accounting.Account;
import brown.assets.value.FullType;
import brown.assets.value.ITradeable;
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
import brown.lab.SpecValGenerator;
import brown.lab.ValuationRegistration;
import brown.messages.Registration;
import brown.server.AgentServer;
import brown.setup.Logging;
import ch.uzh.ifi.ce.mweiss.specval.model.UnsupportedBiddingLanguageException;

import com.esotericsoftware.kryonet.Connection;

/**
 * Implementation of lab3 server.
 * 
 * @author lcamery
 */
public class Lab8Server extends AgentServer {
	private final int GOODNUM = 98;
	private final double VALUESCALE = 1E-6;

	private final int numberOfValuationsPerBidder = 10;
	private final int bundleSizeMean = 5;
	private final double bundleSizeStdez = 5;
	private final Set<Integer> INTS;

	private int numberOfBidders;

	public Lab8Server(int port) {
		super(port, new GameSetup());
		this.INTS = new HashSet<Integer>();
		for (int i = 0; i < GOODNUM; i++) {
			this.INTS.add(i);
		}
		this.numberOfBidders = 0;
	}

	@Override
	protected void onRegistration(Connection connection,
			Registration registration) {
		Integer theID = this.defaultRegistration(connection, registration);
		if (theID == null) {
			return;
		}

		Map<Set<FullType>, Double> value = new HashMap<Set<FullType>, Double>();
//		for (Integer i : this.INTS) {
//			if (Math.random() < .1) {
//				continue;
//			}
//			double nextValue = Math.random() * 50;
//			Set<FullType> theSet = new HashSet<FullType>();
//			theSet.add(new FullType(TradeableType.Custom, i));
//			value.put(theSet, nextValue);
//		}
		this.numberOfBidders++;
		this.theServer.sendToTCP(connection.getID(), new ValuationRegistration(
				theID, value));

		Account oldAccount = bank.get(connections.get(connection));
		Account newAccount = oldAccount.addAll(10000, null);
		bank.put(connections.get(connection), newAccount);

		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
	}

	public void runGame(boolean outcry, boolean firstprice, double reserve)
			throws UnsupportedBiddingLanguageException {
		// Constructs auction according to rules
		Set<ITradeable> theSet = new HashSet<ITradeable>();
		Map<String, FullType> forTakehiro = new HashMap<String, FullType>();
		for (Integer ID : this.INTS) {
			ITradeable newT = new Lab8Good(ID);
			theSet.add(newT);
			forTakehiro.put(ID + "", newT.getType());
		}
		// PaymentRule prule = firstprice ? new FirstPriceRule()
		// : new SecondPriceRule();
		if (outcry) {
			this.manager.open(new Market(new SimpleSecondPriceDemand(),
					new SimpleDemandAllocation(), new OutcryQueryRule(),
					new AnonymousPolicy(), new SamePaymentsTermination(3),
					new SimpleNoJumpActivityRule(), new SimpleInternalState(0,
							theSet)));
		} else {
			this.manager.open(new Market(new SimpleSecondPrice(),
					new SimpleHighestBidderAllocation(), new SealedBidQuery(),
					new AnonymousPolicy(), new OneShotTermination(),
					new SimpleNoJumpActivityRule(), new SimpleInternalState(0,
							theSet)));
		}

		// Gives everyone X seconds to join the auction
		int i = 0;
		while (i < 15) {
			try {
				Thread.sleep(1000);
				Logging.log("[-] setup phase " + i++);
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}

		// Gets valuations
		SpecValGenerator generator = new SpecValGenerator(this.numberOfBidders,
				this.numberOfValuationsPerBidder, this.bundleSizeMean,
				this.bundleSizeStdez, this.VALUESCALE);
		generator.makeValuations();
		Map<String, Map<Set<String>, Double>> allBids = generator
				.convertAllBidsToSimpleBids(generator.allBidderValuations);
		System.out.println("VALUATIONS " + allBids);
		Map<Integer, Map<Set<FullType>, Double>> valuations = new HashMap<Integer, Map<Set<FullType>, Double>>();
		
		for (Entry<Connection, Integer> conn : this.connections.entrySet()) {
			String toRemove = null;
			for (Entry<String, Map<Set<String>, Double>> entry : allBids.entrySet()) {
				Map<Set<String>, Double> each = entry.getValue();
				Map<Set<FullType>, Double> value = new HashMap<Set<FullType>, Double>();
				for (Entry<Set<String>, Double> toAdd : each.entrySet()) {
					Set<FullType> adjusted = new HashSet<FullType>();
					for (String s : toAdd.getKey()) {
						adjusted.add(forTakehiro.get(s));
					}
					value.put(adjusted, toAdd.getValue());
				}
				toRemove = entry.getKey();
				this.theServer.sendToTCP(conn.getKey().getID(), new ValuationRegistration(
						conn.getValue(), value));
				valuations.put(conn.getValue(), value);
			}
			if (toRemove != null) {
				allBids.remove(toRemove);
			}
		}

		// Runs the auction to completion
		// Market market = this.manager.getIMarket(0);
		while (this.manager.getAuctions().size() > 0) {
			try {
				Thread.sleep(2000);
				this.updateAllAuctions();
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}

		// Logs the winner and price
		// Map<BidBundle, Set<ITradeable>> bundles = this.manager.getOneSided(0)
		// .getWinners();
		// Logging.log("[-] auction over");
		// for (BidBundle b : bundles.keySet()) {
		// Logging.log("[-] winner: " + b.getAgent() + " for " + b.getCost());
		// }
		System.out.println("\n\n\n\n\nOUTCOME:");
		for (Account account : this.bank.values()) {
			System.out.println(account);
			System.out.println(account.ID + " got " + account.tradeables.size() + " items with an average cost of " + (10000-account.monies)/account.tradeables.size());
			Map<Set<FullType>, Double> myValue = valuations.get(account.ID);
			Double maxValue = 0.0;
			for (Set<FullType> wantedBundle : myValue.keySet()) {
				if (account.tradeables.contains(wantedBundle)) {
					maxValue = Math.max(maxValue, myValue.get(wantedBundle));
				}
			}
			double linearCost = (10000-account.monies) > 0 ? (10000-account.monies) : account.monies-10000;
			double deltaFuncCost = (10000-account.monies) > 0 ? (10000-account.monies) : Double.MAX_VALUE;
			System.out.println(account.ID + " valued what they got at " + maxValue);
			System.out.println(account.ID + " has a linear utility of " + (maxValue - linearCost));
			System.out.println(account.ID + " has a strict budget utility of " + (maxValue - deltaFuncCost));
			System.out.println();
		}
	}

	public static void main(String[] args)
			throws UnsupportedBiddingLanguageException {
		Lab8Server l3s = new Lab8Server(2121);
		l3s.runGame(true, true, 0);
	}

}
