package brown.auctions.prules;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.value.Tradeable;
import brown.auctions.BidBundle;
import brown.auctions.rules.PaymentRule;
import brown.messages.auctions.Bid;

public class SecondPriceRule implements PaymentRule {
	
	private Bid getSecond(Set<Bid> bids) {
		List<Bid> ordered = new LinkedList<Bid>();
		ordered.addAll(bids);
		Collections.sort(ordered, new Bid.BidComparator(false));
		if (bids.size() >= 2) {
			return ordered.get(1);
		} else {
			return ordered.get(0);
		}
	}

	@Override
	public Map<BidBundle, Set<Tradeable>> getPayments(Map<Integer, Set<Tradeable>> allocations, Set<Bid> bids) {
		Map<BidBundle, Set<Tradeable>> payments = new HashMap<BidBundle, Set<Tradeable>>();
		for (Map.Entry<Integer, Set<Tradeable>> entry : allocations.entrySet()) {
			for (Bid b : bids) {
				if (b.AgentID == entry.getKey()) {
					payments.put(b.Bundle, entry.getValue());
					break;
				}
			}
		}
		
		return payments;
	}

}
