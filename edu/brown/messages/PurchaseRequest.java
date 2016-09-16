package brown.messages;

import brown.markets.PM;

public class PurchaseRequest extends Message {
	public final PM predictionmarket;
	public final int shareYes;
	public final int shareNo;

	public PurchaseRequest(int ID, PM predictionmarket, int shareYes, int shareNo) {
		super(ID);
		this.predictionmarket = predictionmarket;
		this.shareYes = shareYes;
		this.shareNo = shareNo;
	}

}
