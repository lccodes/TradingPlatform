package brown.auctions.prules;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.value.ITradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.rules.PaymentRule;
import brown.messages.auctions.Bid;

public class SecondPriceRule implements PaymentRule {
	
	public SecondPriceRule() {
		
	}

	@Override
	public Map<BidBundle, Set<ITradeable>> getPayments(Map<Integer, Set<ITradeable>> allocations, Set<Bid> bids) {
		Map<BidBundle, Set<ITradeable>> payments = new HashMap<BidBundle, Set<ITradeable>>();
		List<Bid> ordered = new LinkedList<Bid>();
		ordered.addAll(bids);
		Collections.sort(ordered, new Bid.BidComparator(false));
		
		for (Map.Entry<Integer, Set<ITradeable>> entry : allocations.entrySet()) {
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

	@Override
	public PaymentType getPaymentType() {
		return PaymentType.SecondPrice;
	}

}
