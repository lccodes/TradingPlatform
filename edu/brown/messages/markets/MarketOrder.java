package brown.messages.markets;

import brown.agent.Agent;
import brown.auctions.twosided.TwoSidedAuction;
import brown.messages.Message;

public class MarketOrder extends Message {
	public final Integer marketID;
	public final double buyShares;
	public final double sellShares;
	public final double price;
	public final boolean cancel;

	public MarketOrder() {
		super(null);
		this.marketID = null;
		this.buyShares = 0;
		this.sellShares = 0;
		this.price = 0;
		this.cancel = false;
	}
	
	public MarketOrder(Integer ID, TwoSidedAuction market, double buyShares, double sellShares, double price) {
		super(ID);
		this.marketID = market.getID();
		this.buyShares = buyShares;
		this.sellShares = sellShares;
		this.price = price;
		this.cancel = false;
	}
	
	public MarketOrder(Integer ID, Integer marketID, double buyShares, double sellShares, double price) {
		super(ID);
		this.marketID = marketID;
		this.buyShares = buyShares;
		this.sellShares = sellShares;
		this.price = price;
		this.cancel = false;
	}
	
	public MarketOrder(Integer ID, Integer marketID, double buyShares, 
			double sellShares, double price, boolean cancel) {
		super(ID);
		this.marketID = marketID;
		this.buyShares = buyShares;
		this.sellShares = sellShares;
		this.price = price;
		this.cancel = cancel;
	}

	@Override
	public void dispatch(Agent agent) {
		//Noop
	}

}
