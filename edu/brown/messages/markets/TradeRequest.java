package brown.messages.markets;

import brown.auctions.arules.MechanismType;
import brown.auctions.onesided.OneSidedWrapper;
import brown.auctions.twosided.ITwoSidedWrapper;
import brown.messages.Message;

public class TradeRequest extends Message {
	public final ITwoSidedWrapper TMARKET;
	public final OneSidedWrapper OMARKET;
	public final MechanismType MECHANISM;
	
	public TradeRequest() {
		super(null);
		TMARKET = null;
		OMARKET = null;
		MECHANISM = null;
	}

	public TradeRequest(Integer ID, ITwoSidedWrapper market, MechanismType mechanism) {
		super(ID);
		this.TMARKET = market;
		this.OMARKET = null;
		this.MECHANISM = mechanism;
	}
	
	public TradeRequest(Integer ID, OneSidedWrapper market, MechanismType mechanism) {
		super(ID);
		this.TMARKET = null;
		this.OMARKET = market;
		this.MECHANISM = mechanism;
	}

}
