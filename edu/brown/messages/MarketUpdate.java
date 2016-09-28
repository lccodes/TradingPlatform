package brown.messages;

import java.util.List;

import brown.markets.MarketWrapper;

public class MarketUpdate extends Message {
	public final List<MarketWrapper> MARKETS;
	
	public MarketUpdate() {
		super(null);
		MARKETS = null;
	}

	public MarketUpdate(Integer ID, List<MarketWrapper> markets) {
		super(ID);
		this.MARKETS = markets;
	}

}
