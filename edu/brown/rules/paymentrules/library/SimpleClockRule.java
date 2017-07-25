package brown.rules.paymentrules.library;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.accounting.Order;
import brown.assets.value.BasicType;
import brown.bundles.BidBundle;
import brown.bundles.BundleType;
import brown.bundles.MarketState;
import brown.bundles.SimpleBidBundle;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;
import brown.rules.paymentrules.PaymentRule;
import brown.rules.paymentrules.PaymentType;
import brown.setup.Logging;
import brown.tradeables.Tradeable;

public class SimpleClockRule implements PaymentRule {
	private final SimpleBidBundle RESERVE = new SimpleBidBundle(new HashMap<BasicType, MarketState>());

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
