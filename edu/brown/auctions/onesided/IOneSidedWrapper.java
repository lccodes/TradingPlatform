package brown.auctions.onesided;

import java.util.Set;

import brown.assets.value.ITradeable;
import brown.auctions.IMarketWrapper;
import brown.auctions.prules.PaymentType;
import brown.messages.auctions.BidRequest;

public abstract class IOneSidedWrapper implements IMarketWrapper {
	protected final PaymentType PTYPE;
	protected final BidRequest BR;
	
	public IOneSidedWrapper() {
		this.PTYPE = null;
		this.BR = null;
	}
	
	public IOneSidedWrapper(PaymentType type, BidRequest br) {
		this.PTYPE = type;
		this.BR = br;
	}
	
	public PaymentType getPaymentType() {
		return this.PTYPE;
	}
	
	public Set<ITradeable> getTradeables() {
		return this.BR.Goods;
	}
	
	public Integer getAuctionID() {
		return this.BR.AuctionID;
	}
	
	//TODO: On override implement bid
	//TODO: On override implement getQuote
	//TODO: on override implement getAgent
}
