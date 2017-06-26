package brown.rules.activityrules;

import brown.assets.value.FullType;
import brown.bundles.SimpleBidBundle;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;

/**
 * Activity rule for a simultaneous multi-round ascending auction (SMRAA).
 * bids must satisfy inequality [b(q) - (p_t * q) <= B(q) - (p_t * q_t)]
 * where b(q) is the bids across the current good vector, 
 * p_t is the avg price across the bundle,
 * B(q) is the maximal bid so far for the given player, q_t is the current bundle. 
 * @author acoggins
 *
 */
public class SMRAActivity implements ActivityRule {

	@Override
	public boolean isAcceptable(MarketInternalState state, Bid bid) {
		double oldTotalNewBid = 0;
		double newTotalNewBId = 0;
		double oldTotalOldBid = 0;
		double newTotalOldBid = 0;
		SimpleBidBundle bundle = (SimpleBidBundle) bid.Bundle;
		SimpleBidBundle oldBundle = (SimpleBidBundle) state.getAllocation();
		for (FullType demanded : bundle.getDemandSet()) {
			if (bid.AgentID.equals(oldBundle.getBid(demanded).AGENTID)) {
				newTotalNewBId += oldBundle.getBid(demanded).PRICE;
			} else {
				newTotalNewBId += oldBundle.getBid(demanded).PRICE + state.getIncrement();
			}
			oldTotalNewBid += oldBundle.getBid(demanded).PRICE;
		}
		
		for (FullType oldDemanded : oldBundle.getDemandSet()) {
			if (bid.AgentID.equals(oldBundle.getBid(oldDemanded).AGENTID)
					&& bundle.getDemandSet().contains(oldDemanded)) {
				//if agent is on top, get the working price.
				//only add old bid if equal. 
				oldTotalOldBid += oldBundle.getBid(oldDemanded).PRICE;
				newTotalOldBid += oldBundle.getBid(oldDemanded).PRICE;
			} else {
				newTotalOldBid += oldBundle.getBid(oldDemanded).PRICE + state.getIncrement();
			}
		}

		return (newTotalOldBid - oldTotalOldBid) 
				>= (newTotalNewBId - oldTotalNewBid);
	}

}
