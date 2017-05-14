package brown.auctions.activity;

import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.interfaces.ActivityRule;
import brown.auctions.interfaces.MarketInternalState;
import brown.messages.auctions.Bid;

public class VCGEligibility implements ActivityRule {

	@Override
	public boolean isAcceptable(MarketInternalState state, Bid bid) {
		SimpleBidBundle bundle = (SimpleBidBundle) bid.Bundle;
		return bundle.getDemandSet().size() > state.getEligibility();
	}

}
