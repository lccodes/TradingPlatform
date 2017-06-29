package brown.rules.paymentrules.library;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.accounting.Order;
import brown.bundles.BidBundle;
import brown.bundles.BundleType;
import brown.bundles.SimpleBidBundle;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;
import brown.rules.paymentrules.PaymentRule;
import brown.rules.paymentrules.PaymentType;
import brown.tradeables.Tradeable;

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

	@Override
	public List<Order> getPayments(MarketInternalState state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BidBundle getReserve() {
		// TODO Auto-generated method stub
		return null;
	}

}
