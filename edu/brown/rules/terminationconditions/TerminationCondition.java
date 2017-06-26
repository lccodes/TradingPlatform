package brown.rules.terminationconditions;

import brown.marketinternalstates.MarketInternalState;

public interface TerminationCondition {

	public boolean isOver(MarketInternalState state);

}
