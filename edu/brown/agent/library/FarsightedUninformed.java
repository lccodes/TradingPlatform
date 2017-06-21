package brown.agent.library;

import brown.assets.value.TradeableType;
import brown.exceptions.AgentCreationException;
import brown.markets.LMSR;
import brown.setup.Setup;

public class FarsightedUninformed extends ShortsightedInformed {

	public FarsightedUninformed(String host, int port, Setup gameSetup) throws AgentCreationException {
		super(host, port, gameSetup, 1);
	}
	
	@Override
	public void onLMSR(LMSR market) {
		if (market.getTradeableType().TYPE.equals(TradeableType.PredictionYes)) {
			if (market.price() > .5) {
				this.SIGNAL = (.5 + this.TIME) / (this.TIME+1.0);
				super.onLMSR(market);
			}
		} else {
			if (market.price() > .5) {
				this.SIGNAL = .5 / (this.TIME+1.0);
				super.onLMSR(market);
			}
		}
	}

}
