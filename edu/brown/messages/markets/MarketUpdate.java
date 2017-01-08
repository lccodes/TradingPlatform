package brown.messages.markets;

import java.util.List;

import brown.messages.Message;
import brown.securities.SecurityWrapper;

public class MarketUpdate extends Message {
	public final List<SecurityWrapper> MARKETS;
	
	public MarketUpdate() {
		super(null);
		MARKETS = null;
	}

	public MarketUpdate(Integer ID, List<SecurityWrapper> markets) {
		super(ID);
		this.MARKETS = markets;
	}

}
