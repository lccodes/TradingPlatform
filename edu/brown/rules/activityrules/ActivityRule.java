package brown.rules.activityrules;

import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;

/**
 * Interface for all activity rules. This is responsible for determining
 * acceptable trading behaviors and is one of the necessary inputs
 * in the creation of a market. 
 * 
 * @author acoggins
 *
 */
public interface ActivityRule {

	/**
	 * determines if a placed bid is acceptable
	 * @param state
	 * The internal state that holds all internal information of the market.
	 * @param bid
	 * the bid given by an agent. 
	 * @return
	 * boolean stating whether the given bid is acceptable, given the market's
	 * internal state.
	 */
	public boolean isAcceptable(MarketInternalState state, Bid bid);

}
