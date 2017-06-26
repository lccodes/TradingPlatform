package brown.rules.terminationconditions;

import brown.marketinternalstates.MarketInternalState;

public class NoBidsTermination implements brown.rules.terminationconditions.TerminationCondition {
	private final int ENDTIME;
	
	/**
	 * Constructor
	 * @param endtime : how many seconds w/o bid before ending
	 */
	public NoBidsTermination(int endtime) {
		this.ENDTIME = endtime;
	}

	@Override
	public boolean isOver(MarketInternalState state) {
		return state.getTicks() > this.ENDTIME;
	}

}
