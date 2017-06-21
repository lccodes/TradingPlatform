package brown.messages.markets;

import brown.agent.Agent;
import brown.auctions.arules.MechanismType;
import brown.markets.IMarket;
import brown.messages.Message;

public class TradeRequest extends Message {
	public final IMarket MARKET;
	public final MechanismType MECHANISM;
	
	public TradeRequest() {
		super(null);
		MARKET = null;
		MECHANISM = null;
	}

	public TradeRequest(Integer ID, IMarket market, MechanismType mechanism) {
		super(ID);
		this.MARKET = market;
		this.MECHANISM = mechanism;
	}

	@Override
	public void dispatch(Agent agent) {
		this.MARKET.dispatchMessage(agent);
	}

}
