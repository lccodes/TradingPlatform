package brown.auctions.arules;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.value.Tradeable;
import brown.auctions.BidBundle;
import brown.auctions.BundleType;
import brown.auctions.rules.AllocationRule;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;

public class SealedBidRule implements AllocationRule {
	private final int END;
	private final BundleType BT;
	private final boolean ASC;
	private final BidBundle RESERVE;
	
	private int ticks;
	
	public SealedBidRule(BundleType bundleType, boolean ascending,
			int secondsToWait, BidBundle reserve) {
		this.END = secondsToWait;
		this.ticks = 0;
		this.BT = bundleType;
		this.ASC = ascending;
		this.RESERVE = reserve;
	}

	@Override
	public void tick(long time) {
		this.ticks++;
	}
	
	private List<Bid> getSorted(Set<Bid> bids, int length) {
		List<Bid> ordered = new LinkedList<Bid>();
		ordered.addAll(bids);
		Collections.sort(ordered, new Bid.BidComparator(!this.ASC));
		
		List<Bid> topBids = new LinkedList<Bid>();
		for (int i = length-1; i >= 0; i--) {
			if (i >= bids.size()) {
				continue;
			}
			topBids.add(ordered.get(i));
		}
		
		return topBids;
	}

	@Override
	public Map<Integer, Set<Tradeable>> getAllocations(Set<Bid> bids, Set<Tradeable> items) {
		List<Bid> topBids = this.getSorted(bids, 1);
		BidBundle topBid = this.RESERVE;
		Map<Integer, Set<Tradeable>> allocations = new HashMap<Integer, Set<Tradeable>>();
		int i = 0;
		for (Tradeable t : items) {
			if (i < topBids.size() && topBids.get(i).Bundle.getCost() >= topBid.getCost()) {
				Set<Tradeable> theSet = new HashSet<Tradeable>();
				theSet.add(t);
				allocations.put(topBids.get(i).AgentID, theSet);
			} else {
				break;
			}
			i++;
		}
		
		return allocations;
	}

	@Override
	public BidRequest getBidRequest(Set<Bid> bids, Integer ID) {
		for (Bid b : bids) {
			if (b.AgentID == ID) {
				return null;
			}
		}
		
		List<Bid> topBids = this.getSorted(bids, 1);
		BidBundle topBid = this.RESERVE;
		if (topBids.size() > 0 && topBids.get(0).Bundle.getCost() > topBid.getCost()) {
			topBid = topBids.get(0).Bundle;
		}
		
		return new BidRequest(1, null, this.BT, topBid,null);
	}

	@Override
	public boolean isPrivate() {
		return true;
	}

	@Override
	public boolean isOver() {
		return this.END < this.ticks;
	}

	@Override
	public BundleType getBundleType() {
		return this.BT;
	}

}
