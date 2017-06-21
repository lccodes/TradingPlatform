package brown.paymentrules.library;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.bundles.BidBundle;
import brown.bundles.BundleType;
import brown.bundles.MarketState;
import brown.bundles.SimpleBidBundle;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;
import brown.paymentrules.PaymentRule;
import brown.paymentrules.PaymentType;
import brown.setup.Logging;
import brown.tradeables.Tradeable;

public class SimpleSecondPriceDemand implements PaymentRule {

	@Override
	public List<Order> getPayments(MarketInternalState state) {
		List<Order> orders = new LinkedList<Order>();
		if (!state.getAllocation().getType().equals(BundleType.Simple)) {
			Logging.log("[X] Incorrect bundle type " + state.getAllocation().getType());
			return orders;
		}
		
		SimpleBidBundle bundle = (SimpleBidBundle) state.getAllocation();
		for (Tradeable tradeable : state.getTradeables()) {
			MarketState winner = bundle.getBid(tradeable.getType());
			if (winner.AGENTID != null) {
				orders.add(new Order(winner.AGENTID,null,Math.max(winner.PRICE-state.getIncrement(), 0),
						tradeable.getCount(),tradeable));
			}
		}
		//System.out.println("ORDERS " +orders);
		
		return orders;
	}

	@Override
	public PaymentType getPaymentType() {
		return PaymentType.SecondPrice;
	}

	@Override
	public BidBundle getReserve() {
		return new SimpleBidBundle(new HashMap<FullType, MarketState>());
	}

	@Override
	public Map<BidBundle, Set<Tradeable>> getPayments(
			Map<Integer, Set<Tradeable>> allocations, Set<Bid> bids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean permitShort() {
		// TODO Auto-generated method stub
		return false;
	}

}
