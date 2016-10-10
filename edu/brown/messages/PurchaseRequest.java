package brown.messages;

import brown.securities.Security;

public class PurchaseRequest extends Message {
	public final Security market;
	public final int shareYes;
	public final int shareNo;
	
	public PurchaseRequest() {
		super(null);
		market = null;
		shareYes = 0;
		shareNo = 0;
	}

	public PurchaseRequest(int ID, Security predictionmarket, int shareYes, int shareNo) {
		super(ID);
		this.market = predictionmarket;
		this.shareYes = shareYes;
		this.shareNo = shareNo;
	}

}
