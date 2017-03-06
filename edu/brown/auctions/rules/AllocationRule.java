package brown.auctions.rules;

import java.util.Map;
import java.util.Set;

import brown.assets.value.ITradeable;
import brown.auctions.arules.MechanismType;
import brown.auctions.bundles.BundleType;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidReqeust;

public interface AllocationRule {

	void tick(long time);

	Map<Integer, Set<ITradeable>> getAllocations(Set<Bid> bids, Set<ITradeable> items);

	BidReqeust getBidRequest(Set<Bid> bids, Integer iD);

	boolean isPrivate();
	
	boolean isOver();

	BundleType getBundleType();

	Set<Bid> withReserve(Set<Bid> bids);

	boolean isValid(Bid bid, Set<Bid> bids);

	MechanismType getAllocationType();

}
