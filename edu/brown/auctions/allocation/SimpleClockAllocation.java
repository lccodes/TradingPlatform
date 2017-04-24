package brown.auctions.allocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

import brown.assets.value.FullType;
import brown.assets.value.ITradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BidBundle.BidderPrice;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.interfaces.AllocationRule;
import brown.auctions.interfaces.MarketInternalState;
import brown.lab.SpecValGenerator;
import brown.messages.auctions.Bid;
import brown.setup.Logging;

public class SimpleClockAllocation implements AllocationRule {
	private Map<FullType, BidBundle.BidderPrice> lastDemand;
	private final SpecValGenerator GENERATOR;
	private final Map<Integer, String> IDTOSTRING;

	public SimpleClockAllocation(SpecValGenerator generator,
			Map<Integer, String> idToString) {
		this.lastDemand = new HashMap<FullType, BidBundle.BidderPrice>();
		this.GENERATOR = generator;
		this.IDTOSTRING = idToString;
	}

	@Override
	public BidBundle getAllocation(MarketInternalState state) {
		Map<FullType, BidBundle.BidderPrice> highest = new HashMap<FullType, BidBundle.BidderPrice>();
		for (ITradeable trade : state.getTradeables()) {
			BidBundle.BidderPrice lastHigh = this.lastDemand.getOrDefault(
					trade.getType(), new BidBundle.BidderPrice(null, 0));
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
						highest.put(trade.getType(), new BidBundle.BidderPrice(
								bid.AgentID, lastHigh.PRICE));
					}
				} else {
					Logging.log("[X] Incorrect bundle type by " + bid.AgentID
							+ " in auction " + bid.AuctionID);
				}
			}
		}
		System.out.println("last highest " + highest);
		for (Entry<FullType, BidderPrice> entry : highest.entrySet()) {
			this.lastDemand.put(
					entry.getKey(),
					new BidBundle.BidderPrice(entry.getValue().AGENTID, entry
							.getValue().PRICE + state.getIncrement()));
		}
		for (ITradeable t : state.getTradeables()) {
			if (!highest.containsKey(t.getType())) {
				highest.put(t.getType(), this.lastDemand.getOrDefault(
						t.getType(), new BidBundle.BidderPrice(null, state.getIncrement())));
			}
		}
		
		System.out.println("last Demanded " + this.lastDemand);

		Map<String, Map<Set<String>, Double>> forVCGGenerator = new HashMap<String, Map<Set<String>, Double>>();
		Map<String, FullType> recoverTypes = new HashMap<String, FullType>();
		Map<String, BidBundle.BidderPrice> recoverBidders = new HashMap<String, BidBundle.BidderPrice>();
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
						new BidBundle.BidderPrice(bid.AgentID, 0));
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

		Map<FullType, BidBundle.BidderPrice> vcgHighest = new HashMap<FullType, BidBundle.BidderPrice>();
		for (Entry<String, SortedSet<String>> entry : simpleAllocation
				.entrySet()) {
			for (String type : entry.getValue()) {
				vcgHighest.put(
						recoverTypes.get(type),
						new BidBundle.BidderPrice(recoverBidders.get(entry
								.getKey()).AGENTID, getBP(highest, recoverTypes.get(type)).PRICE));
			}
		}

		System.out.println("ALLOC " + vcgHighest);

		for (ITradeable trade : state.getTradeables()) {
			if (!vcgHighest.containsKey(trade.getType())) {
				vcgHighest.put(trade.getType(), this.lastDemand.getOrDefault(
						trade.getType(), new BidBundle.BidderPrice(null, state.getIncrement())));
			}
		}

		state.clearBids();
		return new SimpleBidBundle(vcgHighest);
	}

	public static BidBundle.BidderPrice getBP(
			Map<FullType, BidBundle.BidderPrice> highest, FullType type) {
		BidBundle.BidderPrice attempt = highest.get(type);
		if (attempt != null) {
			return attempt;
		}
		for (Entry<FullType, BidderPrice> entry : highest.entrySet()) {
			if (entry.getKey().equals(type)) {
				return entry.getValue();
			}
		}
		return null;
	}
}
