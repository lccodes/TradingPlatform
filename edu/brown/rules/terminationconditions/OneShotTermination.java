package brown.rules.terminationconditions;

import brown.marketinternalstates.MarketInternalState;


public class OneShotTermination implements TerminationCondition {

	@Override
	public boolean isOver(MarketInternalState state) {
		return state.getBids().size() > 0;
	}

}
