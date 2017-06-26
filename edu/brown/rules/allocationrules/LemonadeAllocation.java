package brown.rules.allocationrules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.accounting.Account;

import brown.assets.value.FullType;
import brown.auctions.arules.MechanismType;
import brown.bundles.BundleType;
import brown.bundles.SimpleBidBundle;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.messages.markets.LemonadeReport;
import brown.tradeables.Tradeable;

public class LemonadeAllocation implements AllocationRule {
	private int ticks = 0;
	private Integer ID;
	private int[] slotz;
	
	public LemonadeAllocation() {
		ID = null;
		this.slotz = new int[12];
	}
	
	public LemonadeAllocation(Integer ID) {
		this.ID = ID;
		this.slotz = new int[12];
	}

	@Override
	public void tick(long time) {
		if (time == -1) {
			this.ticks = 0;
		} else {
			this.ticks++;
		}
	}

	@Override
	public Map<Integer, Set<Tradeable>> getAllocations(Set<Bid> bids, Set<Tradeable> items) {
		@SuppressWarnings("unchecked")
		List<Integer>[] slots = (List<Integer>[]) new List[12];
		Map<Integer, Set<Tradeable>> securities = new HashMap<Integer, Set<Tradeable>>();
		for (Bid b : bids) {
			int index = (int) b.Bundle.getCost() - 1;
			if (slots[index] == null) {
				slots[index] = new LinkedList<Integer>();
			}
			slots[index].add(b.AgentID);
		}
		
		for (int i = 0; i < 12; i++) {
			if (slots[i] == null) {
				continue;
			} else {
				this.slotz[i] = slots[i].size();
			}
			
			double payoff = 0;
			int before = i;
			int after = i;
			for (int next = (i == 11 ? 0 : i+1); next != i; next++) {
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
			payoff /= (double) slots[i].size();
			for (Integer person : slots[i]) {
				Set<Tradeable> goods = new HashSet<Tradeable>();
				final double pay = payoff;
				goods.add(new Contract(person, 1, new FullType(), s -> {
					List<Account> list = new LinkedList<Account>();
					list.add(new Account(null).add(pay));
					return list;
				}));
				securities.put(person, goods);
			}
		}
		
		return securities;
	}

	@Override
	public BidRequest getBidRequest(Set<Bid> bids, Integer ID) {
		for (Bid b : bids) {
			if (b.AgentID == null || b.AgentID.equals(ID)) {
				return null;
			}
		}
		
		return new BidRequest(this.ticks, this.ID, BundleType.Simple, new SimpleBidBundle(1, null, BundleType.Simple), new HashSet<Tradeable>());
	}

	@Override
	public boolean isPrivate() {
		return true;
	}

	@Override
	public boolean isOver() {
		return this.ticks > 2;
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
				|| bid.Bundle.getCost() < 1 || bid.Bundle.getCost() > 12) {
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

	@Override
	public GameReport getReport() {
		return new LemonadeReport(slotz);
	}

}
