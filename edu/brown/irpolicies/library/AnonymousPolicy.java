package brown.irpolicies.library;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Order;
import brown.bundles.BidBundle;
import brown.irpolicies.InformationRevelationPolicy;
import brown.marketinternalstates.MarketInternalState;
import brown.marketinternalstates.SimpleInternalState;
import brown.messages.auctions.Bid;

public class AnonymousPolicy implements InformationRevelationPolicy {

	@Override
	public MarketInternalState handleInfo(Integer ID, MarketInternalState state) {
		BidBundle cleanedAlloc = state.getAllocation();
		cleanedAlloc = cleanedAlloc.wipeAgent(ID);
		MarketInternalState newState = new SimpleInternalState(state.getID(), state.getTradeables());
		for (Bid b : state.getBids()) {
			newState.addBid(b);
		}
		List<Order> newPayments = new LinkedList<Order>();
		for (Order o : state.getPayments()) {
			if (o.TO.equals(ID)) {
				newPayments.add(o);
			} else {
				newPayments.add(o.updateToAgent(null));
			}
		}
		newState.setPayments(newPayments);
		newState.setAllocation(cleanedAlloc);
		return newState;
	}

}
