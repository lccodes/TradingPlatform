package brown.auctions.rules;

import java.util.Map;
import java.util.Set;

import brown.assets.value.Tradeable;
import brown.auctions.BundleType;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;

public interface AllocationRule {

	void tick(long time);

	Map<Integer, Set<Tradeable>> getAllocations(Set<Bid> bids, Set<Tradeable> items);

	BidRequest getBidRequest(Set<Bid> bids, Integer iD);

	boolean isPrivate();
	
	boolean isOver();

	BundleType getBundleType();

}
