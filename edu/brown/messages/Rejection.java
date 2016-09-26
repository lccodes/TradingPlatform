package brown.messages;

public class Rejection extends Message {
	
	public final PurchaseRequest failedPR;
	public final BidRequest failedBR;
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
	
	public Rejection(Integer ID, BidRequest br) {
		super(ID);
		this.failedBR = br;
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
