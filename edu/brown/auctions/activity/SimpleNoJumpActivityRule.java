package brown.auctions.activity;

import brown.auctions.bundles.BundleType;
import brown.auctions.interfaces.ActivityRule;
import brown.auctions.interfaces.MarketInternalState;
import brown.messages.auctions.Bid;

public class SimpleNoJumpActivityRule implements ActivityRule {

	@Override
	public boolean isAcceptable(MarketInternalState state, Bid bid) {
		return bid.Bundle.getType().equals(BundleType.Simple);
	}

}
