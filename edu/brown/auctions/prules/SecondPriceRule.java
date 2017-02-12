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

	@Override
	public Map<BidBundle, Set<Tradeable>> getPayments(Map<Integer, Set<Tradeable>> allocations, Set<Bid> bids) {
		Map<BidBundle, Set<Tradeable>> payments = new HashMap<BidBundle, Set<Tradeable>>();
		List<Bid> ordered = new LinkedList<Bid>();
		ordered.addAll(bids);
		Collections.sort(ordered, new Bid.BidComparator(false));
		
		for (Map.Entry<Integer, Set<Tradeable>> entry : allocations.entrySet()) {
			boolean next = false;
			for (Bid b : ordered) {
				if (next) {
					payments.put(b.Bundle.wipeAgent(entry.getKey()), entry.getValue());
					break;
				}
				if (b.AgentID.equals(entry.getKey())) {
					next = true;
				}
			}
		}
		
		return payments;
	}

}
