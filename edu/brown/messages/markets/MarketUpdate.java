package brown.messages.markets;

import brown.auctions.OneSidedWrapper;
import brown.auctions.TwoSidedAuction;
import brown.auctions.arules.AllocationType;
import brown.messages.Message;

public class MarketUpdate extends Message {
	public final TwoSidedAuction TMARKET;
	public final OneSidedWrapper OMARKET;
	public final AllocationType MECHANISM;
	
	public MarketUpdate() {
		super(null);
		TMARKET = null;
		OMARKET = null;
		MECHANISM = null;
	}

	public MarketUpdate(Integer ID, TwoSidedAuction market, AllocationType mechanism) {
		super(ID);
		this.TMARKET = market;
		this.OMARKET = null;
		this.MECHANISM = mechanism;
	}

}
