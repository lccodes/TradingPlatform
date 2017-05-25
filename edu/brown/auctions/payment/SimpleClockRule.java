package brown.auctions.payment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.assets.value.Tradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.MarketState;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.interfaces.MarketInternalState;
import brown.auctions.interfaces.PaymentRule;
import brown.auctions.prules.PaymentType;
import brown.setup.Logging;

public class SimpleClockRule implements PaymentRule {
	private final SimpleBidBundle RESERVE = new SimpleBidBundle(new HashMap<FullType, MarketState>());

	@Override
	public List<Order> getPayments(MarketInternalState state) {
		List<Order> orders = new LinkedList<Order>();
		if (!state.getAllocation().getType().equals(BundleType.Simple)) {
			Logging.log("[X] Incorrect bundle type " + state.getAllocation().getType());
			return orders;
		}
		
		SimpleBidBundle bundle = (SimpleBidBundle) state.getAllocation();
		for(Tradeable trade : state.getTradeables()) {
			MarketState winner = bundle.getBid(trade.getType());
			if (winner.AGENTID != null) {
				orders.add(new Order(winner.AGENTID,null,winner.PRICE,
						trade.getCount(),trade));
			}
		}
		
		System.out.println("Payment " + orders);
		return orders;
	}

	@Override
	public PaymentType getPaymentType() {
		return PaymentType.VCG;
	}

	@Override
	public BidBundle getReserve() {
		return this.RESERVE;
	}

}
