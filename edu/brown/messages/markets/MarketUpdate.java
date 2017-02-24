package brown.messages.markets;

import brown.auctions.arules.AllocationType;
import brown.auctions.onesided.OneSidedWrapper;
import brown.auctions.twosided.TwoSidedWrapper;
import brown.messages.Message;

public class MarketUpdate extends Message {
	public final TwoSidedWrapper TMARKET;
	public final OneSidedWrapper OMARKET;
	public final AllocationType MECHANISM;
	
	public MarketUpdate() {
		super(null);
		TMARKET = null;
		OMARKET = null;
		MECHANISM = null;
	}

	public MarketUpdate(Integer ID, TwoSidedWrapper market, AllocationType mechanism) {
		super(ID);
		this.TMARKET = market;
		this.OMARKET = null;
		this.MECHANISM = mechanism;
	}

}
