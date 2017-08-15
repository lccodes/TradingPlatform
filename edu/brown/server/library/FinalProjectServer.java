package brown.server.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import brown.rules.allocationrules.SimpleDemandAllocation;
import brown.rules.allocationrules.SimpleHighestBidderAllocation;
import brown.assets.accounting.Account;
import brown.assets.accounting.Order;
import brown.assets.value.BasicType;
import brown.bundles.MarketState;
import brown.marketinternalstates.SimpleInternalState;
import brown.markets.Market;
import brown.messages.Registration;
import brown.rules.paymentrules.library.SimpleSecondPrice;
import brown.rules.paymentrules.library.SimpleSecondPriceDemand;
import brown.registrations.ValuationRegistration;
import brown.rules.activityrules.SMRAActivity;
import brown.rules.activityrules.SimpleNoJumpActivityRule;
import brown.rules.irpolicies.library.AnonymousPolicy;
import brown.rules.queryrules.library.OutcryQueryRule;
import brown.rules.queryrules.library.SealedBidQuery;
import brown.rules.terminationconditions.OneShotTermination;
import brown.rules.terminationconditions.SamePaymentsTermination;
import brown.server.AgentServer;
import brown.setup.Logging;
import brown.setup.library.LabGameSetup;
import brown.specval.SpecValGenerator;
import brown.tradeables.FinalProjectGood;
import brown.tradeables.Tradeable;
import ch.uzh.ifi.ce.mweiss.specval.model.UnsupportedBiddingLanguageException;

import com.esotericsoftware.kryonet.Connection;

/**
 * Implementation of lab3 server.
 * 
 * @author lcamery
 */
public class FinalProjectServer extends AgentServer {
	private final int GOODNUM = 98;
	private final double VALUESCALE = 1E-6;

	private final int numberOfValuationsPerBidder = 25;
	private final int bundleSizeMean = 7;
	private final double bundleSizeStdez = 2;
	private final Set<Integer> INTS;

	private int numberOfBidders;

	public FinalProjectServer(int port) {
		super(port, new LabGameSetup());
		this.INTS = new HashSet<Integer>();
		for (int i = 0; i < GOODNUM; i++) {
			this.INTS.add(i);
		}
		this.numberOfBidders = 0;
	}

	@Override
	protected void onRegistration(Connection connection, Registration registration) {
		Integer theID = this.defaultRegistration(connection, registration);
		if (theID == null) {
			return;
		}

		Map<Set<BasicType>, Double> value = new HashMap<Set<BasicType>, Double>();
		// for (Integer i : this.INTS) {
		// if (Math.random() < .1) {
		// continue;
		// }
		// double nextValue = Math.random() * 50;
		// Set<FullType> theSet = new HashSet<FullType>();
		// theSet.add(new FullType(TradeableType.Custom, i));
		// value.put(theSet, nextValue);
		// }
		this.numberOfBidders++;
		this.theServer.sendToTCP(connection.getID(), new ValuationRegistration(theID, value));

		Account oldAccount = acctManager.getAccount(connections.get(connection));
		Account newAccount = oldAccount.addAll(10000, null);
		acctManager.setAccount(connections.get(connection), newAccount);

		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
	}

	public void runGame(boolean finalround) throws UnsupportedBiddingLanguageException {
		// Constructs auction according to rules
		Set<Tradeable> theSet = new HashSet<Tradeable>();
		Map<String, BasicType> forTakehiro = new HashMap<String, BasicType>();
		for (Integer ID : this.INTS) {
			Tradeable newT = new FinalProjectGood(ID);
			theSet.add(newT);
			forTakehiro.put(ID + "", newT.getType());
		}

		// Gives everyone X seconds to join the auction
		int i = 0;
		while (i < 10) { //CHANGE FOR MORE OR LESS JOIN TIME
			try {
				Thread.sleep(1000);
				Logging.log("[-] setup phase " + i++);
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}

		// Gets valuations
		SpecValGenerator generator = new SpecValGenerator(this.numberOfBidders, this.numberOfValuationsPerBidder,
				this.bundleSizeMean, this.bundleSizeStdez, this.VALUESCALE);
		generator.makeValuations();
		Map<String, Map<Set<String>, Double>> allBids = generator
				.convertAllBidsToSimpleBids(generator.allBidderValuations);
		System.out.println("VALUATIONS " + allBids);
		Map<Integer, Map<Set<BasicType>, Double>> valuations = new HashMap<Integer, Map<Set<BasicType>, Double>>();
		Map<Integer, String> intToString = new HashMap<Integer, String>();

		for (Entry<Connection, Integer> conn : this.connections.entrySet()) {
			String toRemove = null;
			for (Entry<String, Map<Set<String>, Double>> entry : allBids.entrySet()) {
				Map<Set<String>, Double> each = entry.getValue();
				for (Entry<Set<String>, Double> toAdd : each.entrySet()) {
					Map<Set<BasicType>, Double> value = new HashMap<Set<BasicType>, Double>();
					Set<BasicType> adjusted = new HashSet<BasicType>();
					for (String s : toAdd.getKey()) {
						adjusted.add(forTakehiro.get(s));
					}
					value.put(adjusted, toAdd.getValue());
					this.theServer.sendToUDP(conn.getKey().getID(), new ValuationRegistration(conn.getValue(), value));
					if (valuations.containsKey(conn.getValue())) {
						value.putAll(valuations.get(conn.getValue()));
					}
					valuations.put(conn.getValue(), value);
				}
				intToString.put(conn.getValue(), entry.getKey());
				toRemove = entry.getKey();
			}
			if (toRemove != null) {
				allBids.remove(toRemove);
			}
		}
		
		this.manager.open(new Market(new SimpleSecondPriceDemand(), new SimpleDemandAllocation(),
				new OutcryQueryRule(), new AnonymousPolicy(), new SamePaymentsTermination(3),
				new SMRAActivity(), new SimpleInternalState(0, theSet)));
		// Runs the auction to completion
		Market market = this.manager.getIMarket(0);
		while (!market.isOver()) {
			try {
				Thread.sleep(4000);
				this.updateAllAuctions(!finalround);
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}
		
		if (finalround) {
			Map<BasicType, MarketState> reservePrices = new HashMap<BasicType, MarketState>();
			for (Order o : market.getOrders()) {
				reservePrices.put(o.GOOD.getType(), new MarketState(null, o.COST));
			}
			this.manager.open(new Market(new SimpleSecondPrice(), new SimpleHighestBidderAllocation(),
					new SealedBidQuery(), new AnonymousPolicy(), new OneShotTermination(),
					new SimpleNoJumpActivityRule(), new SimpleInternalState(1, theSet, reservePrices)));
			Market market2 = this.manager.getIMarket(1);
			boolean first = true;
			while (!market2.isOver()) {
				try {
					this.updateAllAuctions(false);
					Thread.sleep(first ? 60000 : 1000);
					first = false;
				} catch (InterruptedException e) {
					Logging.log("[+] woken: " + e.getMessage());
				}
			}
			List<Order> firstOrders = this.manager.getIMarket(0).getOrders();
			List<Order> secondOrders = this.manager.getIMarket(1).getOrders();
			double totalRevenue0 = 0, totalRevenue1 = 0;
			for (Order o : firstOrders) {
				totalRevenue0 += o.COST;
			}
			for (Order o : secondOrders) {
				totalRevenue1 += o.COST;
			}
			if (totalRevenue0 > totalRevenue1) {
				System.out.println("Using SMRA");
				this.manager.close(this, 1, null);
			} else {
				System.out.println("Using sealed bids");
				this.manager.close(this, 0, null);
			}
			this.updateAllAuctions(true);
		} else {
			this.updateAllAuctions(true);
		}

		// Logs the winner and price
		// Map<BidBundle, Set<ITradeable>> bundles = this.manager.getOneSided(0)
		// .getWinners();
		// Logging.log("[-] auction over");
		// for (BidBundle b : bundles.keySet()) {
		// Logging.log("[-] winner: " + b.getAgent() + " for " + b.getCost());
		// }
		System.out.println("\n\n\n\n\nOUTCOME:");
		for (Account account : this.acctManager.getAccounts()) {
			System.out.println(account);
			System.out.println(account.ID + " got " + account.tradeables.size() + " items with an average cost of "
					+ (10000 - account.monies) / account.tradeables.size());
			Map<Set<BasicType>, Double> myValue = valuations.get(account.ID);
			Double maxValue = 0.0;
			for (Set<BasicType> wantedBundle : myValue.keySet()) {
				int contains = 0;
				for (Tradeable t : account.tradeables) {
					if (wantedBundle.contains(t.getType())) {
						contains++;
					}
				}
				if (contains == wantedBundle.size()) {
					maxValue = Math.max(maxValue, myValue.get(wantedBundle));
				}
			}
			double linearCost = (10000 - account.monies) > 0 ? (10000 - account.monies) : account.monies - 10000;
			System.out.println(account.ID + " valued what they got at " + maxValue);
			System.out.println(account.ID + " has a linear utility of " + (maxValue - linearCost));
			System.out.println(account.ID + " has a strict budget utility of " + (maxValue - linearCost > 0 ? maxValue-linearCost : -1*Double.MAX_VALUE));
			System.out.println();
		}
	}

	public static void main(String[] args) throws UnsupportedBiddingLanguageException {
		FinalProjectServer l3s = new FinalProjectServer(2121);
		// true, false = SMRA
		// true, true = SMRA w/ sealed bid final round
		// false, false, = Clock auction
		// false, true  = clock w/ VCG
		l3s.runGame(true);
	}

}
