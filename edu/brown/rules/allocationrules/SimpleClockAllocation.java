package brown.rules.allocationrules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

import brown.assets.value.FullType;
import brown.auctions.arules.MechanismType;
import brown.bundles.BidBundle;
import brown.bundles.BundleType;
import brown.bundles.MarketState;
import brown.bundles.SimpleBidBundle;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.setup.Logging;
import brown.tradeables.Tradeable;
import brown.valuegenerator.SpecValGenerator;

public class SimpleClockAllocation implements AllocationRule {
	private Map<FullType, MarketState> lastDemand;
	private final SpecValGenerator GENERATOR;
	private final Map<Integer, String> IDTOSTRING;
	private double maxRevenue;

	public SimpleClockAllocation(SpecValGenerator generator,
			Map<Integer, String> idToString) {
		this.lastDemand = new HashMap<FullType, MarketState>();
		this.GENERATOR = generator;
		this.IDTOSTRING = idToString;
		this.maxRevenue = 0;
	}

	@Override
	public BidBundle getAllocation(MarketInternalState state) {
		Map<FullType, MarketState> highest = new HashMap<FullType, MarketState>();
		for (Tradeable trade : state.getTradeables()) {
			MarketState lastHigh = this.lastDemand.getOrDefault(
					trade.getType(), new MarketState(null, 0));
			boolean updated = false;
			for (Bid bid : state.getBids()) {
				if (updated) {
					break;
				}

				if (bid.Bundle.getType().equals(BundleType.Simple)) {
					SimpleBidBundle bundle = (SimpleBidBundle) bid.Bundle;
					if (bundle.isDemanded(trade.getType())
							&& !bid.AgentID.equals(lastHigh.AGENTID)) {
						updated = true;
						highest.put(trade.getType(), new MarketState(
								bid.AgentID, lastHigh.PRICE));
					}
				} else {
					Logging.log("[X] Incorrect bundle type by " + bid.AgentID
							+ " in auction " + bid.AuctionID);
				}
			}
		}
		System.out.println("last highest " + highest);
		//clock price ascension
		for (Entry<FullType, MarketState> entry : highest.entrySet()) {
			this.lastDemand.put(
					entry.getKey(),
					new MarketState(entry.getValue().AGENTID, entry
							.getValue().PRICE + state.getIncrement()));
		}
		for (Tradeable t : state.getTradeables()) {
			if (!highest.containsKey(t.getType())) {
				highest.put(t.getType(), this.lastDemand.getOrDefault(
						t.getType(), new MarketState(null, state.getIncrement())));
			}
		}
		
		System.out.println("last Demanded " + this.lastDemand);

		Map<String, Map<Set<String>, Double>> forVCGGenerator = new HashMap<String, Map<Set<String>, Double>>();
		Map<String, FullType> recoverTypes = new HashMap<String, FullType>();
		Map<String, MarketState> recoverBidders = new HashMap<String, MarketState>();
		for (Bid bid : state.getBids()) {
			double totalForBid = 0;
			if (bid.Bundle.getType().equals(BundleType.Simple)) {
				SimpleBidBundle bundle = (SimpleBidBundle) bid.Bundle;
				Map<Set<String>, Double> bundlePriceGenerator = new HashMap<Set<String>, Double>();
				Set<String> types = new HashSet<String>();
				for (FullType t : bundle.getDemandSet()) {
					types.add("" + t.ID);
					recoverTypes.put("" + t.ID, t);
					if (bundle.isDemanded(t)) {
						totalForBid += SimpleClockAllocation.getBP(highest, t).PRICE;
					}
				}
				bundlePriceGenerator.put(types, totalForBid);
				if (forVCGGenerator.containsKey(this.IDTOSTRING
						.get(bid.AgentID))) {
					Map<Set<String>, Double> part = forVCGGenerator
							.get(this.IDTOSTRING.get(bid.AgentID));
					part.putAll(bundlePriceGenerator);
					forVCGGenerator.put(this.IDTOSTRING.get(bid.AgentID), part);
				} else {
					forVCGGenerator.put(this.IDTOSTRING.get(bid.AgentID),
							bundlePriceGenerator);
				}

				recoverBidders.put(this.IDTOSTRING.get(bid.AgentID),
						new MarketState(bid.AgentID, 0));
			}
		}

		if (forVCGGenerator.size() < 2) {
			return new SimpleBidBundle(highest);
		}
		System.out.println("FORVCG " + forVCGGenerator);
		this.GENERATOR.runVCGWithGivenBids(forVCGGenerator);
		Map<String, SortedSet<String>> simpleAllocation = SpecValGenerator
				.getVcgAllocationSimpleBid(this.GENERATOR.auctionResult);
		SpecValGenerator.printVcgResults(this.GENERATOR.auctionResult);
		double revenue = this.GENERATOR.auctionResult.getPayment().getTotalPayments();
		if (revenue > this.maxRevenue) {
			state.setMaximizingRevenue(true);
			this.maxRevenue = revenue;
		} else {
			state.setMaximizingRevenue(false);
		}

		Map<FullType, MarketState> vcgHighest = new HashMap<FullType, MarketState>();
		for (Entry<String, SortedSet<String>> entry : simpleAllocation
				.entrySet()) {
			for (String type : entry.getValue()) {
				vcgHighest.put(
						recoverTypes.get(type),
						new MarketState(recoverBidders.get(entry
								.getKey()).AGENTID, getBP(highest, recoverTypes.get(type)).PRICE));
			}
		}

		System.out.println("ALLOC " + vcgHighest);

		for (Tradeable trade : state.getTradeables()) {
			if (!vcgHighest.containsKey(trade.getType())) {
				vcgHighest.put(trade.getType(), this.lastDemand.getOrDefault(
						trade.getType(), new MarketState(null, state.getIncrement())));
			}
		}

		state.clearBids();
		return new SimpleBidBundle(vcgHighest);
	}

	public static MarketState getBP(
			Map<FullType, MarketState> highest, FullType type) {
		MarketState attempt = highest.get(type);
		if (attempt != null) {
			return attempt;
		}
		for (Entry<FullType, MarketState> entry : highest.entrySet()) {
			if (entry.getKey().equals(type)) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public void tick(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Integer, Set<Tradeable>> getAllocations(Set<Bid> bids,
			Set<Tradeable> items) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BidRequest getBidRequest(Set<Bid> bids, Integer iD) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPrivate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BundleType getBundleType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Bid> withReserve(Set<Bid> bids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(Bid bid, Set<Bid> bids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MechanismType getAllocationType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameReport getReport() {
		// TODO Auto-generated method stub
		return null;
	}
}
