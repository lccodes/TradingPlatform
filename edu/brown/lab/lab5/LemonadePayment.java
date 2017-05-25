package brown.lab.lab5;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import brown.assets.value.Tradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.prules.PaymentType;
import brown.auctions.rules.PaymentRule;
import brown.messages.auctions.Bid;

public class LemonadePayment implements PaymentRule {

	@Override
	public Map<BidBundle, Set<Tradeable>> getPayments(
			Map<Integer, Set<Tradeable>> allocations, Set<Bid> bids) {
		Map<BidBundle, Set<Tradeable>> payments = new HashMap<BidBundle, Set<Tradeable>>();
		for (Map.Entry<Integer, Set<Tradeable>> alloc : allocations.entrySet()) {
			payments.put(new SimpleBidBundle(0, alloc.getKey(), BundleType.Simple), alloc.getValue());
		}
		
		return payments;
	}

	@Override
	public PaymentType getPaymentType() {
		return PaymentType.Lemonade;
	}

	@Override
	public boolean permitShort() {
		return true;
	}

}
