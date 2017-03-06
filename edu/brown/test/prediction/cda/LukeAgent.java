package brown.test.prediction.cda;

import brown.exceptions.AgentCreationException;
import brown.lab.lab5.Lab5Agent;
import brown.messages.markets.MarketUpdate;
import brown.securities.mechanisms.cda.CDAWrapper;

public class LukeAgent extends Lab5Agent {
	private boolean once = false;

	public LukeAgent(String host, int port) throws AgentCreationException {
		super(host, port);
	}

	@Override
	public void onContinuousDoubleAuction(CDAWrapper market) {
		if (Math.random() > .5 && !this.once) {
			market.buy(this, .5, 10);
			this.once = true;
		} else if (!this.once){
			market.sell(this, .5, 10);
			this.once = true;
		}
	}

	@Override
	public void onMarketUpdate(MarketUpdate marketUpdate) {
		// TODO Auto-generated method stub
		
	}

}
