package brown.allocationrules;

import java.util.Map;
import java.util.Set;

import brown.auctions.arules.MechanismType;
import brown.bundles.BidBundle;
import brown.bundles.BundleType;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.tradeables.Tradeable;

public interface AllocationRule {

	void tick(long time);
	
	public BidBundle getAllocation(MarketInternalState state);

	Map<Integer, Set<Tradeable>> getAllocations(Set<Bid> bids, Set<Tradeable> items);

	BidRequest getBidRequest(Set<Bid> bids, Integer iD);

	boolean isPrivate();
	
	boolean isOver();

	BundleType getBundleType();

	Set<Bid> withReserve(Set<Bid> bids);

	boolean isValid(Bid bid, Set<Bid> bids);

	MechanismType getAllocationType();

	GameReport getReport();

}
