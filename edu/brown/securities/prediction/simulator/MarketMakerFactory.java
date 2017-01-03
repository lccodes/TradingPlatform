package brown.securities.prediction.simulator;

import java.util.LinkedList;
import java.util.List;

import brown.securities.prediction.structures.PMBackend;

public class MarketMakerFactory {
	private List<PMBackend> marketMakers;
	
	public MarketMakerFactory() {
		this.marketMakers = new LinkedList<PMBackend>();
	}
	
	public MarketMakerFactory add(PMBackend marketmaker) {
		this.marketMakers.add(marketmaker);
		return this;
	}
	
	public List<PMBackend> make() {
		return this.marketMakers;
	}

}
