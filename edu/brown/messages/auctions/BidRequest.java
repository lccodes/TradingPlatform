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
	
	/**
	 * Kryo requires empty constructor
	 * DO NOT USE
	 */
	public BidRequest() {
		super(null);
		this.AuctionID = null;
		this.BundleType = null;
		this.CurrentPrice = 0;
		this.Good = null;
		this.HighBidder = false;
		this.HighBidderID = -1;
	}
	
	/**
	 * BidRequest for when an auction wants to solicit
	 * bids from the agents in the game
	 * @param ID : BR ID
	 * @param auctionID : auction's ID
	 * @param bundleType : bundleType that the auction wants
	 * @param currentPrice : current price in the auction
	 * @param good : good being auctioned
	 * @param highBidder : true if this agent is the high bidder
	 * @param highBidderID : highBidder (nullable if private) ID of high bidder
	 */
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
