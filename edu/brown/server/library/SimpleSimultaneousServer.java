package brown.server.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import brown.activityrules.SimpleNoJumpActivityRule;
import brown.allocationrules.SimpleClockAllocation;
import brown.allocationrules.SimpleHighestBidderAllocation;
import brown.assets.accounting.Account;
import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.irpolicies.library.AnonymousPolicy;
import brown.marketinternalstates.SimpleInternalState;
import brown.markets.Market;
import brown.messages.Registration;
import brown.paymentrules.library.SimpleClockRule;
import brown.paymentrules.library.SimpleSecondPrice;
import brown.queryrules.library.OutcryQueryRule;
import brown.queryrules.library.SealedBidQuery;
import brown.registrations.ValuationRegistration;
import brown.server.AgentServer;
import brown.setup.Logging;
import brown.setup.library.LabGameSetup;
import brown.terminationconditions.OneShotTermination;
import brown.terminationconditions.SamePaymentsTermination;
import brown.tradeables.SimGood;
import brown.tradeables.Tradeable;
import brown.valuegenerator.SpecValGenerator;
import ch.uzh.ifi.ce.mweiss.specval.model.UnsupportedBiddingLanguageException;

import com.esotericsoftware.kryonet.Connection;

/**
 * Implementation of lab3 server.
 * 
 * @author acoggins
 */
public class SimpleSimultaneousServer extends AgentServer {
	private final int GOODNUM = 98;
	private final double VALUESCALE = 1E-6;

	private final int numberOfValuationsPerBidder = 10;
	private final int bundleSizeMean = 5;
	private final double bundleSizeStdez = 5;
	private final Set<Integer> INTS;

	private int numberOfBidders;

	public SimpleSimultaneousServer(int port) {
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

		Map<Set<FullType>, Double> value = new HashMap<Set<FullType>, Double>();
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

	public void runGame(double reserve)
			throws UnsupportedBiddingLanguageException {
		// Constructs auction according to rules
		Set<Tradeable> theSet = new HashSet<Tradeable>();
		Map<String, FullType> forTakehiro = new HashMap<String, FullType>();
		for (Integer ID : this.INTS) {
			Tradeable newT = new SimGood(ID);
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
		System.out.println("VALUATIONS" + allBids);
		Map<Integer, Map<Set<FullType>, Double>> valuations = new HashMap<Integer, Map<Set<FullType>, Double>>();
		Map<Integer, String> intToString = new HashMap<Integer, String>();

		for (Entry<Connection, Integer> conn : this.connections.entrySet()) {
			String toRemove = null;
			for (Entry<String, Map<Set<String>, Double>> entry : allBids.entrySet()) {
				Map<Set<String>, Double> each = entry.getValue();
				for (Entry<Set<String>, Double> toAdd : each.entrySet()) {
					Map<Set<FullType>, Double> value = new HashMap<Set<FullType>, Double>();
					Set<FullType> adjusted = new HashSet<FullType>();
					for (String s : toAdd.getKey()) {
						adjusted.add(forTakehiro.get(s));
					}
					value.put(adjusted, toAdd.getValue());
					this.theServer.sendToTCP(conn.getKey().getID(), new ValuationRegistration(conn.getValue(), value));
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

			this.manager.open(new Market(new SimpleSecondPrice(), new SimpleHighestBidderAllocation(),
					new SealedBidQuery(), new AnonymousPolicy(), new OneShotTermination(),
					new SimpleNoJumpActivityRule(), new SimpleInternalState(0, theSet)));
			Market market = this.manager.getIMarket(0);
			while (!market.isOver()) {
				try {
					Thread.sleep(4000);
					//Thread.sleep(2000);
					this.updateAllAuctions(false);
				} catch (InterruptedException e) {
					Logging.log("[+] woken: " + e.getMessage());
				}
			}
			List<Order> orders = market.getOrders();
			double totalRevenue = 0;
			for (Order o : orders) {
				totalRevenue += o.COST;
			}
		
				System.out.println("Using sealed bids");
				System.out.println("Total Revenue: " + totalRevenue);
				this.manager.close(this, 0, null);
			this.updateAllAuctions(true);
		

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
			Map<Set<FullType>, Double> myValue = valuations.get(account.ID);
			Double maxValue = 0.0;
			for (Set<FullType> wantedBundle : myValue.keySet()) {
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
		SimpleSimultaneousServer l3s = new SimpleSimultaneousServer(2121);
		// true, false = SMRA
		// true, true = SMRA w/ sealed bid final round
		// false, false, = Clock auction
		// false, true  = clock w/ VCG
		l3s.runGame(0);
	}

}
