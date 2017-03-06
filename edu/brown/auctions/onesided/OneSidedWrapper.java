package brown.auctions.onesided;

import java.util.Set;

import brown.assets.value.ITradeable;
import brown.auctions.bundles.BundleType;
import brown.auctions.prules.PaymentType;
import brown.messages.auctions.BidReqeust;

public abstract class OneSidedWrapper {
	protected final PaymentType PTYPE;
	protected final BidReqeust BR;
	
	public OneSidedWrapper() {
		this.PTYPE = null;
		this.BR = null;
	}
	
	public OneSidedWrapper(PaymentType type, BidReqeust br) {
		this.PTYPE = type;
		this.BR = br;
	}
	
	public PaymentType getPaymentType() {
		return this.PTYPE;
	}
	
	public Set<ITradeable> getTradeables() {
		return this.BR.Goods;
	}
	
	public BundleType getBundleType() {
		return this.BR.TYPE;
	}
	
	public Integer getAuctionID() {
		return this.BR.AuctionID;
	}
	
	//TODO: On override implement bid
	//TODO: On override implement quote
}
