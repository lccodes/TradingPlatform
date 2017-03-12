package brown.auctions.prules;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import brown.assets.value.ITradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.rules.PaymentRule;
import brown.messages.auctions.Bid;

public class FirstPriceRule implements PaymentRule {
	
	public FirstPriceRule() {
		
	}

	@Override
	public Map<BidBundle, Set<ITradeable>> getPayments(Map<Integer, Set<ITradeable>> allocations, Set<Bid> bids) {
		Map<BidBundle, Set<ITradeable>> payments = new HashMap<BidBundle, Set<ITradeable>>();
		for (Map.Entry<Integer, Set<ITradeable>> entry : allocations.entrySet()) {
			for (Bid b : bids) {
				if (b.AgentID != null && b.AgentID.equals(entry.getKey())) {
					payments.put(b.Bundle, entry.getValue());
					break;
				}
			}
		}
		
		return payments;
	}

	@Override
	public PaymentType getPaymentType() {
		return PaymentType.FirstPrice;
	}

}
