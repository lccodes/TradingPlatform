package brown.messages;

import brown.messages.auctions.Bid;
import brown.messages.markets.PurchaseRequest;
import brown.messages.trades.TradeRequest;

public class Rejection extends Message {
	
	public final PurchaseRequest failedPR;
	public final Bid failedBR;
	public final TradeRequest failedTR;
	
	public Rejection() {
		super(null);
		this.failedBR = null;
		this.failedPR = null;
		this.failedTR = null;
	}

	public Rejection(Integer ID, PurchaseRequest pr) {
		super(ID);
		this.failedBR = null;
		this.failedPR = pr;
		this.failedTR = null;
	}
	
	public Rejection(Integer ID, Bid bid) {
		super(ID);
		this.failedBR = bid;
		this.failedPR = null;
		this.failedTR = null;
	}
	
	public Rejection(Integer ID, TradeRequest tr) {
		super(ID);
		this.failedBR = null;
		this.failedPR = null;
		this.failedTR = tr;
	}

}
