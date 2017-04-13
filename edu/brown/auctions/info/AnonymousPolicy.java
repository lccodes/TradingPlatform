package brown.auctions.info;

import brown.auctions.bundles.BidBundle;
import brown.auctions.interfaces.InformationRevelationPolicy;
import brown.auctions.interfaces.MarketInternalState;
import brown.auctions.state.SimpleInternalState;
import brown.messages.auctions.Bid;

public class AnonymousPolicy implements InformationRevelationPolicy {

	@Override
	public MarketInternalState handleInfo(Integer ID, MarketInternalState state) {
		BidBundle cleanedAlloc = state.getAllocation();
		cleanedAlloc.wipeAgent(ID);
		MarketInternalState newState = new SimpleInternalState(state.getID(), state.getTradeables());
		for (Bid b : state.getBids()) {
			newState.addBid(b);
		}
		newState.setPayments(state.getPayments());
		newState.setAllocation(cleanedAlloc);
		return newState;
	}

}
