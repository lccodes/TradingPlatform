package brown.test.prediction.cda;

import brown.exceptions.AgentCreationException;
import brown.lab.UnitCDAWrapper;
import brown.lab.lab4.Lab4Agent;
import brown.messages.markets.GameReport;
import brown.securities.mechanisms.cda.CDAWrapper;

public class LukeAgent extends Lab4Agent {
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
	public void onMarketUpdate(GameReport marketUpdate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onContinuousDoubleAuction(UnitCDAWrapper unitCDAWrapper) {
		// TODO Auto-generated method stub
		
	}

}
