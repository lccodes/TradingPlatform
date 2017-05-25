package brown.auctions.termination;

import brown.auctions.interfaces.MarketInternalState;
import brown.auctions.interfaces.TerminationCondition;


public class OneShotTermination implements TerminationCondition {

	@Override
	public boolean isOver(MarketInternalState state) {
		return state.getBids().size() > 0;
	}

}
