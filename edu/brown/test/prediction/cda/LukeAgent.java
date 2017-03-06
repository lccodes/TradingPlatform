package brown.test.prediction.cda;

import brown.exceptions.AgentCreationException;
import brown.securities.mechanisms.cda.CDAWrapper;

public class LukeAgent extends Lab4Agent {
	//private boolean once = false;

	public LukeAgent(String host, int port) throws AgentCreationException {
		super(host, port);
	}

	@Override
	public void onContinuousDoubleAuction(CDAWrapper market) {
		//if (this.myCoin && !this.once) {
			market.buy(this, .5, 10);
		//	this.once = true;
		//} else if (!this.once){
			market.sell(this, .5, 10);
		//	this.once = true;
		//}
	}

}
