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

public class SimpleSecondPrice implements PaymentRule {
	private final Map<BasicType, MarketState> RESERVE;
	
	public SimpleSecondPrice(Map<BasicType, MarketState> reserve) {
		if (reserve == null) {
			this.RESERVE = new HashMap<BasicType, MarketState>();
		} else {
			this.RESERVE = reserve;
		}
	}
	
	public SimpleSecondPrice() {
		this.RESERVE = new HashMap<BasicType, MarketState>();
	}

	@Override
	public List<Order> getPayments(MarketInternalState state) {
		List<Order> orders = new LinkedList<Order>();
		if (!state.getAllocation().getType().equals(BundleType.Simple)) {
			Logging.log("[X] Incorrect bundle type " + state.getAllocation().getType());
			return orders;
		}
		SimpleBidBundle alloc = (SimpleBidBundle) state.getAllocation();
		MarketState def = new MarketState(null,0);
		for (Tradeable trade : state.getTradeables()) {
		  //this is empty
			MarketState bp = alloc.getBid(trade.getType());
			System.out.println("BP " + bp);
			if (bp == null || bp.AGENTID == null) {
				continue;
			}
			MarketState current = this.RESERVE.getOrDefault(trade.getType(), def);
			for (Bid bid : state.getBids()) {
				if (bid.Bundle.getType().equals(BundleType.Simple)) {
					SimpleBidBundle bundle = (SimpleBidBundle) bid.Bundle;
					MarketState otherbid = bundle.getBid(trade.getType());
					System.out.println("ob " + otherbid);
					//second price logic
					if (otherbid != null && otherbid.PRICE > current.PRICE && bp.PRICE >= otherbid.PRICE) {
						current = new MarketState(bp.AGENTID, otherbid.PRICE);
					}
				} else {
					Logging.log("[X] Incorrect bundle type by " + bid.AgentID + " in auction " + bid.AuctionID);
				}
			}
			//System.out.println(current);
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
