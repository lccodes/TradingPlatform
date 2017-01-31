package brown.messages.markets;

import brown.messages.Message;
import brown.securities.Security;

public class LimitOrder extends Message {
	public final Security market;
	public final double buyShares;
	public final double sellShares;
	public final double price;

	public LimitOrder() {
		super(null);
		this.market = null;
		this.buyShares = 0;
		this.sellShares = 0;
		this.price = 0;
	}
	
	public LimitOrder(Integer ID, Security market, double buyShares, double sellShares, double price) {
		super(ID);
		this.market = market;
		this.buyShares = buyShares;
		this.sellShares = sellShares;
		this.price = price;
	}

}
