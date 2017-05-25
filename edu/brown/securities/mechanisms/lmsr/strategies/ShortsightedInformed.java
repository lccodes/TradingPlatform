package brown.securities.mechanisms.lmsr.strategies;

import brown.assets.value.TradeableType;
import brown.exceptions.AgentCreationException;
import brown.securities.mechanisms.lmsr.LMSR;
import brown.setup.Setup;

public class ShortsightedInformed extends ExperimentalAgent {

	public ShortsightedInformed(String host, int port, Setup gameSetup, double signal) throws AgentCreationException {
		super(host, port, gameSetup, signal);
	}

	@Override
	public void onLMSR(LMSR market) {
		if (market.getTradeableType().TYPE.equals(TradeableType.PredictionYes)) {
			if (this.CURRENTTIME++ != this.TIME) { //TODO: make this work; yes'ers are missing
				return;
			}
			
			//System.out.println("MP-SIGNAL" + market.price() + " " + this.SIGNAL);
			if (market.price() < this.SIGNAL) {
				System.out.println("YES>TIME " + this.CURRENTTIME + " " + this.SIGNAL);
				double shareNum = market.priceToShares(this.SIGNAL);
				//System.out.println("Signal " + this.SIGNAL + " MP "+ market.price() +" Price to share num: " + shareNum);
				double sharesToBuy = Math.min(shareNum, market.moniesToShares(this.BUDGET));
				System.out.println(sharesToBuy);
				//System.out.println("Budget " + this.BUDGET +  " w/ mTS " + market.moniesToShares(this.BUDGET));
				//if (sharesToBuy != 0) {
					market.buy(this, sharesToBuy, 1);
				//}
			}
		} else {
			if (this.CURRENTTIME != this.TIME) {
				return;
			}
			
			//System.out.println("MP-SIGNAL" + market.price() + " " + (1-this.SIGNAL));
			if (market.price() < (1 - this.SIGNAL)) {
				System.out.println("NO>TIME " + this.CURRENTTIME + " " + this.SIGNAL);
				double shareNum = market.priceToShares(1-this.SIGNAL);
				//System.out.println("Signal " + (1-this.SIGNAL) + " MP "+ market.price() +" Price to share num: " + shareNum);
				double sharesToBuy = Math.min(shareNum, market.moniesToShares(this.BUDGET));
				//System.out.println("Budget " + this.BUDGET +  " w/ mTS " + market.moniesToShares(this.BUDGET));
				System.out.println(sharesToBuy);
				//if (sharesToBuy != 0) {
					market.buy(this, sharesToBuy, 1);
				//}
			}
		}
	}
	
}
