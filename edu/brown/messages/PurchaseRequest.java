package brown.messages;

import brown.securities.prediction.PM;

public class PurchaseRequest extends Message {
	public final PM predictionmarket;
	public final int shareYes;
	public final int shareNo;
	
	public PurchaseRequest() {
		super(null);
		predictionmarket = null;
		shareYes = 0;
		shareNo = 0;
	}

	public PurchaseRequest(int ID, PM predictionmarket, int shareYes, int shareNo) {
		super(ID);
		this.predictionmarket = predictionmarket;
		this.shareYes = shareYes;
		this.shareNo = shareNo;
	}

}
