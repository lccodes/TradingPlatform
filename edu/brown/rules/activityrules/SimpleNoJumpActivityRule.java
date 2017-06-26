package brown.rules.activityrules;

import brown.bundles.BundleType;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;

/**
 * Simple no-jump activity rule. Dictates that only Simple
 * bid bundles will be accepted.
 * @author acoggins
 *
 */
public class SimpleNoJumpActivityRule implements ActivityRule {

	@Override
	public boolean isAcceptable(MarketInternalState state, Bid bid) {
		return bid.Bundle.getType().equals(BundleType.Simple);
	}

}
