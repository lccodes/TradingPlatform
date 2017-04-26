package brown.securities.mechanisms.lmsr.strategies;

import brown.assets.value.TradeableType;
import brown.exceptions.AgentCreationException;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
import brown.setup.Setup;

public class ShortsightedInformed extends ExperimentalAgent {

	public ShortsightedInformed(String host, int port, Setup gameSetup, double signal) throws AgentCreationException {
		super(host, port, gameSetup, signal);
		System.out.println(signal);
	}

	@Override
	public void onLMSR(LMSRWrapper market) {
		if (market.getTradeableType().TYPE.equals(TradeableType.PredictionYes)) {
			if (this.CURRENTTIME++ != this.TIME) {
				return;
			}
			
			System.out.println(market.price() + " " + this.SIGNAL);
			if (market.price() < this.SIGNAL) {
				double shareNum = market.priceToShares(this.SIGNAL);
				System.out.println("Signal " + this.SIGNAL + " MP "+ market.price() +" Price to share num: " + shareNum);
				double sharesToBuy = Math.min(shareNum, market.moniesToShares(this.BUDGET));
				System.out.println("Budget " + this.BUDGET +  " w/ mTS " + market.moniesToShares(this.BUDGET));
				if (sharesToBuy != 0) {
					market.buy(this, sharesToBuy, 1);
				}
			}
		} else {
			if (this.CURRENTTIME != this.TIME) {
				return;
			}
			
			if (market.price() < (1 - this.SIGNAL)) {
				double shareNum = market.priceToShares(1-this.SIGNAL);
				double sharesToBuy = Math.min(shareNum, market.moniesToShares(this.BUDGET));
				if (sharesToBuy != 0) {
					market.buy(this, sharesToBuy, 1);
				}
			}
		}
	}
	
}
