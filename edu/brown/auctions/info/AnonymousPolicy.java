package brown.auctions.info;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Order;
import brown.auctions.bundles.BidBundle;
import brown.auctions.interfaces.InformationRevelationPolicy;
import brown.auctions.interfaces.MarketInternalState;
import brown.auctions.state.SimpleInternalState;
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
