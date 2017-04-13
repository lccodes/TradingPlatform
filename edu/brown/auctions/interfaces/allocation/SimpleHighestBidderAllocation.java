package brown.auctions.interfaces.allocation;

import java.util.HashMap;
import java.util.Map;

import brown.assets.value.FullType;
import brown.assets.value.ITradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.interfaces.AllocationRule;
import brown.auctions.interfaces.MarketInternalState;
import brown.messages.auctions.Bid;

public class SimpleHighestBidderAllocation implements AllocationRule {
	private final Map<FullType, BidBundle.BidderPrice> RESERVE;
	
	public SimpleHighestBidderAllocation(Map<FullType, BidBundle.BidderPrice> reserve) {
		this.RESERVE = reserve;
	}
	
	public SimpleHighestBidderAllocation() {
		this.RESERVE = new HashMap<FullType, BidBundle.BidderPrice>();
	}

	@Override
	public BidBundle getAllocation(MarketInternalState state) {
		Map<FullType, BidBundle.BidderPrice> highest = new HashMap<FullType, BidBundle.BidderPrice>();
		BidBundle.BidderPrice def = new BidBundle.BidderPrice(null,0);
		for (ITradeable trade : state.getTradeables()) {
			BidBundle.BidderPrice maxBidder = this.RESERVE.getOrDefault(trade.getType(), def);
			for (Bid bid : state.getBids()) {
				if (bid.Bundle.getType().equals(BundleType.Simple)) {
					SimpleBidBundle bundle = (SimpleBidBundle) bid.Bundle;
					BidBundle.BidderPrice bp = bundle.getBid(trade.getType());
					if (bp != null && bp.PRICE > maxBidder.PRICE) {
						maxBidder = new BidBundle.BidderPrice(bid.AgentID, bp.PRICE);
					}
				}
			}
			highest.put(trade.getType(), maxBidder);
		}
		
		return new SimpleBidBundle(highest);
	}

}
