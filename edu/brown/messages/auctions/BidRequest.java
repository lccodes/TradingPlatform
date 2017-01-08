package brown.messages.auctions;

import brown.assets.value.Good;
import brown.auctions.BundleType;
import brown.messages.Message;

public class BidRequest extends Message {
	public final Integer AuctionID;
	public final BundleType BundleType;
	public final Good Good;
	
	public final double CurrentPrice;
	public final boolean HighBidder;
	public final Integer HighBidderID;
	
	public BidRequest() {
		super(null);
		this.AuctionID = null;
		this.BundleType = null;
		this.CurrentPrice = 0;
		this.Good = null;
		this.HighBidder = false;
		this.HighBidderID = -1;
	}
	
	public BidRequest(int ID, Integer auctionID, BundleType bundleType,
			double currentPrice, Good good, boolean highBidder,
			Integer highBidderID) {
		super(ID);
		this.AuctionID = auctionID;
		this.BundleType = bundleType;
		this.CurrentPrice = currentPrice;
		this.Good = good;
		this.HighBidder = highBidder;
		this.HighBidderID = highBidderID;
	}

}
