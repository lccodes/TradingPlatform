package brown.lab.lab5;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.value.ITradeable;
import brown.auctions.arules.MechanismType;
import brown.auctions.bundles.BundleType;
import brown.auctions.rules.AllocationRule;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidReqeust;

public class LemonadeAllocation implements AllocationRule {

	@Override
	public void tick(long time) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<Integer, Set<ITradeable>> getAllocations(Set<Bid> bids, Set<ITradeable> items) {
		@SuppressWarnings("unchecked")
		List<Integer>[] slots = (List<Integer>[]) new List[12];
		Map<Integer, Set<ITradeable>> securities = new HashMap<Integer, Set<ITradeable>>();
		for (Bid b : bids) {
			int index = (int) b.Bundle.getCost();
			if (slots[index] == null) {
				slots[index] = new LinkedList<Integer>();
			}
			slots[index].add(b.AgentID);
		}
		
		for (int i = 0; i < 12; i++) {
			double payoff = 0;
			int before = i;
			int after = i;
			for (int next = (i == 11 ? 0 : i+1); next < 12 || next < i; next++) {
				if (slots[next] !=  null) {
					if (after == i) {
						after = next;
					}
					before = next;
				}
				
				if (next == 11) {
					next = -1;
				}
			}
			payoff = after > i ? after - i : 11-i + after;
			payoff += before < i ? i - before : before;
			payoff /= slots[i].size();
			for (Integer person : slots[i]) {
				// Put contract in with this value
				securities.put(person, )
			}
		}
		
		return securities;
	}

	@Override
	public BidReqeust getBidRequest(Set<Bid> bids, Integer iD) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPrivate() {
		return true;
	}

	@Override
	public boolean isOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BundleType getBundleType() {
		return BundleType.Simple;
	}

	@Override
	public Set<Bid> withReserve(Set<Bid> bids) {
		return bids;
	}

	@Override
	public boolean isValid(Bid bid, Set<Bid> bids) {
		if (bid.AgentID == null || bid.Bundle == null
				|| bid.Bundle.getCost() < 0 || bid.Bundle.getCost() > 11) {
			return false;
		}
		
		for (Bid b : bids) {
			if (b.AgentID.equals(bid.AgentID)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public MechanismType getAllocationType() {
		return MechanismType.Lemonade;
	}

}
