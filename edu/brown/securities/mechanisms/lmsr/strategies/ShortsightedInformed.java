package brown.securities.mechanisms.lmsr.strategies;

import brown.assets.value.TradeableType;
import brown.exceptions.AgentCreationException;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
import brown.setup.Setup;

public class ShortsightedInformed extends ExperimentalAgent {

	public ShortsightedInformed(String host, int port, Setup gameSetup, double signal, int timeToGo) throws AgentCreationException {
		super(host, port, gameSetup, signal, timeToGo);
	}

	@Override
	public void onLMSR(LMSRWrapper market) {
		if (this.CURRENTTIME++ != this.TIME) {
			return;
		}
		
		if (market.getTradeableType().TYPE.equals(TradeableType.PredictionYes)) {
			if (market.price() < this.SIGNAL) {
				double shareNum = market.priceToShares(this.SIGNAL);
				double totalCost = Math.min(this.BUDGET, market.moniesToShares(shareNum));
				double sharesToBuy = market.priceToShares(totalCost);
				if (sharesToBuy != 0) {
					market.buy(this, sharesToBuy, 1);
				}
			}
		} else {
			if (market.price() > (1 - this.SIGNAL)) {
				double shareNum = market.priceToShares(1 - this.SIGNAL);
				double totalCost = Math.min(this.BUDGET, market.moniesToShares(shareNum));
				double sharesToBuy = market.priceToShares(totalCost);
				if (sharesToBuy != 0) {
					market.buy(this, sharesToBuy, 1);
				}
			}
		}
	}
	
}
