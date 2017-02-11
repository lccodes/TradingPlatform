package brown.messages.auctions;

import java.util.Set;

import brown.assets.value.Tradeable;
import brown.auctions.BidBundle;
import brown.auctions.BundleType;
import brown.messages.Message;

public class BidRequest extends Message {
	public final Integer AuctionID;
	public final BundleType BundleType;
	public final Set<Tradeable> Goods;
	public final BidBundle Current;
	
	/**
	 * Kryo requires empty constructor
	 * DO NOT USE
	 */
	public BidRequest() {
		super(null);
		this.AuctionID = null;
		this.BundleType = null;
		this.Current = null;
		this.Goods = null;
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
			BidBundle bundle, Set<Tradeable> goods) {
		super(ID);
		this.AuctionID = auctionID;
		this.BundleType = bundleType;
		this.Current = bundle;
		this.Goods = goods;
	}

}
