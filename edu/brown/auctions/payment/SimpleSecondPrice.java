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
import brown.setup.Logging;

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
			Logging.log("[X] Incorrect bundle type " + state.getAllocation().getType());
			return orders;
		}
		SimpleBidBundle alloc = (SimpleBidBundle) state.getAllocation();
		BidBundle.BidderPrice def = new BidBundle.BidderPrice(null,0);
		for (ITradeable trade : state.getTradeables()) {
			BidBundle.BidderPrice bp = alloc.getBid(trade.getType());
			if (bp == null || bp.AGENTID == null) {
				continue;
			}
			BidBundle.BidderPrice current = this.RESERVE.getOrDefault(trade.getType(), def);
			for (Bid bid : state.getBids()) {
				if (bid.Bundle.getType().equals(BundleType.Simple)) {
					SimpleBidBundle bundle = (SimpleBidBundle) bid.Bundle;
					BidBundle.BidderPrice otherbid = bundle.getBid(trade.getType());
					if (otherbid != null && otherbid.PRICE > current.PRICE && bp.PRICE > otherbid.PRICE) {
						current = new BidBundle.BidderPrice(bp.AGENTID, otherbid.PRICE);
					}
				} else {
					Logging.log("[X] Incorrect bundle type by " + bid.AgentID + " in auction " + bid.AuctionID);
				}
			}
			orders.add(new Order(bp.AGENTID,null,current.PRICE,trade.getCount(),trade));
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