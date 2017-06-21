package brown.paymentrules.library;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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

public class SecondPriceRule implements PaymentRule {
	final boolean SHORT;
	
	public SecondPriceRule() {
		this.SHORT = false;
	}
	
	public SecondPriceRule(boolean permitShort) {
		this.SHORT = permitShort;
	}

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

	@Override
	public PaymentType getPaymentType() {
		return PaymentType.SecondPrice;
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
