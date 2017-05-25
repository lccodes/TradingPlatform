package brown.auctions.allocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import brown.assets.value.FullType;
import brown.assets.value.Tradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.bundles.MarketState;
import brown.auctions.interfaces.AllocationRule;
import brown.auctions.interfaces.MarketInternalState;
import brown.messages.auctions.Bid;
import brown.setup.Logging;

public class SimpleDemandAllocation implements AllocationRule {
	private Map<FullType, MarketState> lastDemand;

	public SimpleDemandAllocation() {
		this.lastDemand = new HashMap<FullType, MarketState>();
	}

	@Override
	public BidBundle getAllocation(MarketInternalState state) {
		//System.out.println("WHUT " + state.getBids());
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

		state.clearBids();
		for (Entry<FullType, MarketState> entry : highest.entrySet()) {
			this.lastDemand.put(
					entry.getKey(),
					new MarketState(entry.getValue().AGENTID, entry
							.getValue().PRICE + state.getIncrement()));
		}
		for (Tradeable t : state.getTradeables()) {
			if (!highest.containsKey(t.getType())) {
				highest.put(t.getType(), this.lastDemand.getOrDefault(
						t.getType(), new MarketState(null, 0)));
			}
		}
		//System.out.println(this.lastDemand);
		return new SimpleBidBundle(highest);
	}
}
