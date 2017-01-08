package brown.messages.auctions;

import brown.assets.value.Good;
import brown.auctions.BundleType;
import brown.messages.Message;

public class BidRequest extends Message {
	public final Integer AuctionID;
	public final BundleType BundleType;
	public final int CurrentPrice;
	public final Good Good;
	
	public BidRequest() {
		super(null);
		this.AuctionID = null;
		this.BundleType = null;
		this.CurrentPrice = 0;
		this.Good = null;
	}
	
	public BidRequest(int ID, Integer auctionID, BundleType bundleType,
			int currentPrice, Good good) {
		super(ID);
		this.AuctionID = auctionID;
		this.BundleType = bundleType;
		this.CurrentPrice = currentPrice;
		this.Good = good;
	}

}
