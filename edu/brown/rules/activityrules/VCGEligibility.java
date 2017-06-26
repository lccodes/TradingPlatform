package brown.rules.activityrules;

import brown.bundles.SimpleBidBundle;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;

/**
 * activity rule for VCG auction. the bundle being bid must contain
 * more elements than the current state eligibility. 
 * @author acoggins
 *
 */
public class VCGEligibility implements ActivityRule {

	@Override
	public boolean isAcceptable(MarketInternalState state, Bid bid) {
		SimpleBidBundle bundle = (SimpleBidBundle) bid.Bundle;
		return bundle.getDemandSet().size() > state.getEligibility();
	}

}
