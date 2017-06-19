package brown.auctions.allocation;

import java.util.HashMap;
import java.util.Map;

import brown.assets.value.FullType;
import brown.assets.value.Tradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.MarketState;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.interfaces.AllocationRule;
import brown.auctions.interfaces.MarketInternalState;
import brown.messages.auctions.Bid;
import brown.setup.Logging;


public class SimpleHighestBidderAllocation implements AllocationRule {
	private final Map<FullType, MarketState> RESERVE;
	public SimpleHighestBidderAllocation(Map<FullType, MarketState> reserve) {
		this.RESERVE = reserve;
	}
	public SimpleHighestBidderAllocation() {
		this.RESERVE = new HashMap<FullType, MarketState>();
	}
	
	@Override
	public BidBundle getAllocation(MarketInternalState state) {
		System.out.println("BIDS? " + state.getBids());
		Map<FullType, MarketState> highest = new HashMap<FullType, MarketState>();
		MarketState def = new MarketState(null,0);
		for (Tradeable trade : state.getTradeables()) {
			MarketState maxBidder = this.RESERVE.getOrDefault(trade.getType(), def);
			for (Bid bid : state.getBids()) {
				if (bid.Bundle.getType().equals(BundleType.Simple)) {
					SimpleBidBundle bundle = (SimpleBidBundle) bid.Bundle;
					MarketState bp = bundle.getBid(trade.getType());
					if (bp != null && bp.PRICE > maxBidder.PRICE) {
						maxBidder = new MarketState(bid.AgentID, bp.PRICE);
					}
				} else {
					Logging.log("[X] Incorrect bundle type by " + bid.AgentID + " in auction " + bid.AuctionID);
				}
			}
			highest.put(trade.getType(), maxBidder);
		}
		
		return new SimpleBidBundle(highest);
	}

}
