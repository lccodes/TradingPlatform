package brown.auctions.interfaces;

import brown.auctions.bundles.BidBundle;

public interface AllocationRule {

	public BidBundle getAllocation(MarketInternalState state);

}
