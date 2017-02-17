package brown.auctions.prules;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import brown.assets.value.Tradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.rules.PaymentRule;
import brown.messages.auctions.Bid;

public class FirstPriceRule implements PaymentRule {

	@Override
	public Map<BidBundle, Set<Tradeable>> getPayments(Map<Integer, Set<Tradeable>> allocations, Set<Bid> bids) {
		Map<BidBundle, Set<Tradeable>> payments = new HashMap<BidBundle, Set<Tradeable>>();
		for (Map.Entry<Integer, Set<Tradeable>> entry : allocations.entrySet()) {
			for (Bid b : bids) {
				if (b.AgentID != null && b.AgentID.equals(entry.getKey())) {
					payments.put(b.Bundle, entry.getValue());
					break;
				}
			}
		}
		
		return payments;
	}

}
