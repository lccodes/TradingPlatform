package brown.securities.prediction.simulator;

import java.util.LinkedList;
import java.util.List;

import brown.markets.LMSRBackend;

public class MarketMakerFactory {
	private List<LMSRBackend> marketMakers;
	
	public MarketMakerFactory() {
		this.marketMakers = new LinkedList<LMSRBackend>();
	}
	
	public MarketMakerFactory add(LMSRBackend marketmaker) {
		this.marketMakers.add(marketmaker);
		return this;
	}
	
	public List<LMSRBackend> make() {
		return this.marketMakers;
	}

}
