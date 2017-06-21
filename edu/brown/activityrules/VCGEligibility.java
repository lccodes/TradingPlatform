package brown.activityrules;

import brown.bundles.SimpleBidBundle;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;

public class VCGEligibility implements ActivityRule {

	@Override
	public boolean isAcceptable(MarketInternalState state, Bid bid) {
		SimpleBidBundle bundle = (SimpleBidBundle) bid.Bundle;
		return bundle.getDemandSet().size() > state.getEligibility();
	}

}
