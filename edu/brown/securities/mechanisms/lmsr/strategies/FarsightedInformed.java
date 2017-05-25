package brown.securities.mechanisms.lmsr.strategies;

import brown.assets.value.TradeableType;
import brown.exceptions.AgentCreationException;
import brown.securities.mechanisms.lmsr.LMSR;
import brown.setup.Setup;

public class FarsightedInformed extends ShortsightedInformed {

	public FarsightedInformed(String host, int port, Setup gameSetup, double signal)
			throws AgentCreationException {
		super(host, port, gameSetup, signal);
	}

	@Override
	public void onLMSR(LMSR market) {
		this.SIGNAL = market.getTradeableType().TYPE.equals(TradeableType.PredictionYes) ?
				(this.SIGNAL + this.TIME*market.price())/(1.0 + this.TIME) :
					((1-this.SIGNAL) + this.TIME*market.price())/(1.0 + this.TIME);
		super.onLMSR(market);
	}

}
