package brown.securities.mechanisms.lmsr.strategies;

import brown.assets.value.TradeableType;
import brown.exceptions.AgentCreationException;
import brown.securities.mechanisms.lmsr.LMSR;
import brown.setup.Setup;

public class ShortsightedUninformed extends ShortsightedInformed {

	public ShortsightedUninformed(String host, int port, Setup gameSetup) throws AgentCreationException {
		super(host, port, gameSetup, 1);
	}

	@Override
	public void onLMSR(LMSR market) {
		if (market.getTradeableType().TYPE.equals(TradeableType.PredictionYes)) {
			if (market.price() > .5) {
				this.SIGNAL = 1;
				super.onLMSR(market);
			}
		} else {
			if (market.price() > .5) {
				this.SIGNAL = 0;
				super.onLMSR(market);
			}
		}
	}

}
