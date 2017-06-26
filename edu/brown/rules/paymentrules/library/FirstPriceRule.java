package brown.rules.paymentrules.library;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.accounting.Order;
import brown.bundles.BidBundle;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;
import brown.paymentrules.PaymentRule;
import brown.paymentrules.PaymentType;
import brown.tradeables.Tradeable;

public class FirstPriceRule implements PaymentRule {
	private final boolean SHORT;
	
	public FirstPriceRule() {
		this.SHORT = false;
	}
	
	public FirstPriceRule(boolean permitShort) {
		this.SHORT = permitShort;
	}

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

	@Override
	public PaymentType getPaymentType() {
		return PaymentType.FirstPrice;
	}

	@Override
	public boolean permitShort() {
		return this.SHORT;
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
