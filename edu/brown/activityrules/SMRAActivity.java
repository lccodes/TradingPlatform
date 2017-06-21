package brown.activityrules;

import brown.assets.value.FullType;
import brown.bundles.SimpleBidBundle;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;

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
