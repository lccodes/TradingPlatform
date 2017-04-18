package brown.securities.mechanisms.lmsr.strategies;

import brown.assets.value.TradeableType;
import brown.auctions.wrappers.SimpleWrapper;
import brown.exceptions.AgentCreationException;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
import brown.setup.Setup;

public class CRRAAgent extends KellyAgent {
	protected final double RISK_N;

	public CRRAAgent(String host, int port, Setup gameSetup, double risk_n) throws AgentCreationException {
		super(host, port, gameSetup);
		this.RISK_N = 1 - risk_n;
	}

	@Override
	public void onLMSR(LMSRWrapper market) {
		if (this.RISK_N == 1) {
			super.onLMSR(market);
			return;
		}
		
		double p_m = market.getTradeableType().TYPE == TradeableType.PredictionYes ? 
				market.price() : 1-market.price();
		double ratio = (this.belief*(1 - p_m)) / (p_m*(1 - this.belief));
		double top = p_m * (Math.pow(ratio, 1/this.RISK_N) - 1);
		double shares = (this.money / p_m) * (top/(top+1));
		if (market.getTradeableType().TYPE == TradeableType.PredictionYes) {
			market.buy(this, shares, 1);
		} else {
			market.sell(this, shares, 1);
		}
	}

}
