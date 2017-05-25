package brown.auctions.activity;

import brown.auctions.interfaces.ActivityRule;
import brown.auctions.interfaces.MarketInternalState;
import brown.messages.auctions.Bid;

public class OneShotActivity implements ActivityRule {

	@Override
	public boolean isAcceptable(MarketInternalState state, Bid bid) {
		return !(state.getBids().contains(bid));
	}

}
