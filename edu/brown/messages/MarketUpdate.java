package brown.messages;

import java.util.List;

import brown.markets.PredictionMarket;

public class MarketUpdate extends Message {
	public final List<PredictionMarket> MARKETS;

	public MarketUpdate(Integer ID, List<PredictionMarket> markets) {
		super(ID);
		this.MARKETS = markets;
	}

}
