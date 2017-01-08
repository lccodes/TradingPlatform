package brown.messages.markets;

import brown.messages.Message;
import brown.securities.Security;

public class PurchaseRequest extends Message {
	public final Security market;
	public final int buy;
	public final int sell;
	
	public PurchaseRequest() {
		super(null);
		market = null;
		buy = 0;
		sell = 0;
	}

	public PurchaseRequest(int ID, Security predictionmarket, int shareYes, int shareNo) {
		super(ID);
		this.market = predictionmarket;
		this.buy = shareYes;
		this.sell = shareNo;
	}

}
