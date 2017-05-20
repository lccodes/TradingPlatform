package brown.auctions.payment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.assets.value.ITradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.MarketState;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.interfaces.MarketInternalState;
import brown.auctions.interfaces.PaymentRule;
import brown.auctions.prules.PaymentType;
import brown.setup.Logging;

public class SimpleSecondPriceDemand implements PaymentRule {

	@Override
	public List<Order> getPayments(MarketInternalState state) {
		List<Order> orders = new LinkedList<Order>();
		if (!state.getAllocation().getType().equals(BundleType.Simple)) {
			Logging.log("[X] Incorrect bundle type " + state.getAllocation().getType());
			return orders;
		}
		
		SimpleBidBundle bundle = (SimpleBidBundle) state.getAllocation();
		for (ITradeable tradeable : state.getTradeables()) {
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

}
