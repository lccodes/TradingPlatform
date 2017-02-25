package brown.messages.markets;

import brown.auctions.twosided.TwoSidedAuction;
import brown.messages.Message;

public class MarketOrder extends Message {
	public final TwoSidedAuction market;
	public final double buyShares;
	public final double sellShares;
	public final double price;

	public MarketOrder() {
		super(null);
		this.market = null;
		this.buyShares = 0;
		this.sellShares = 0;
		this.price = 0;
	}
	
	public MarketOrder(Integer ID, TwoSidedAuction market, double buyShares, double sellShares, double price) {
		super(ID);
		this.market = market;
		this.buyShares = buyShares;
		this.sellShares = sellShares;
		this.price = price;
	}

}
