package brown.auctions.activity;

import java.util.List;

import brown.assets.accounting.Order;
import brown.auctions.interfaces.ActivityRule;
import brown.auctions.interfaces.MarketInternalState;
import brown.messages.auctions.Bid;

public class SMRAActivity implements ActivityRule {
	private double totalPayments = 0;
	private List<Order> oldPayments = null;

	@Override
	public boolean isAcceptable(MarketInternalState state, Bid bid) {
		if (state.getReserve().getType().equals(bid.Bundle.getType())) {
			if (oldPayments != state.getPayments()) {
				totalPayments = 0;
				for (Order o : state.getPayments()) {
					totalPayments += o.COST;
				}
			}
			return bid.Bundle.getCost() > totalPayments;
		}
		return false;
	}

}
