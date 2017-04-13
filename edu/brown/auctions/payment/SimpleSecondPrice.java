package brown.auctions.payment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.assets.value.ITradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.interfaces.MarketInternalState;
import brown.auctions.interfaces.PaymentRule;
import brown.auctions.prules.PaymentType;
import brown.messages.auctions.Bid;

public class SimpleSecondPrice implements PaymentRule {
	private final Map<FullType, BidBundle.BidderPrice> RESERVE;
	
	public SimpleSecondPrice(Map<FullType, BidBundle.BidderPrice> reserve) {
		if (reserve == null) {
			this.RESERVE = new HashMap<FullType, BidBundle.BidderPrice>();
		} else {
			this.RESERVE = reserve;
		}
	}
	
	public SimpleSecondPrice() {
		this.RESERVE = new HashMap<FullType, BidBundle.BidderPrice>();
	}

	@Override
	public List<Order> getPayments(MarketInternalState state) {
		List<Order> orders = new LinkedList<Order>();
		if (!state.getAllocation().getType().equals(BundleType.Simple)) {
			return orders;
		}
		SimpleBidBundle alloc = (SimpleBidBundle) state.getAllocation();
		BidBundle.BidderPrice def = new BidBundle.BidderPrice(null,0);
		for (ITradeable trade : state.getTradeables()) {
			BidBundle.BidderPrice bp = alloc.getBid(trade.getType());
			BidBundle.BidderPrice current = this.RESERVE.getOrDefault(trade.getType(), def);
			for (Bid b : state.getBids()) {
				if (b.Bundle.getType().equals(BundleType.Simple)) {
					SimpleBidBundle newBidBundle = (SimpleBidBundle) b.Bundle;
					BidBundle.BidderPrice newBid = newBidBundle.getItemCost(trade.getType());
					if (newBid != null && bp.PRICE > newBid.PRICE && newBid.PRICE > current.PRICE) {
						current = new BidBundle.BidderPrice(b.AgentID, newBid.PRICE);
					}
				}
			}
			if (current.AGENTID != null) {
				orders.add(new Order(current.AGENTID,null,current.PRICE,trade.getCount(),trade));
			}
		}
		
		return orders;
	}

	@Override
	public PaymentType getPaymentType() {
		return PaymentType.SecondPrice;
	}

	@Override
	public BidBundle getReserve() {
		return new SimpleBidBundle(this.RESERVE);
	}

}
