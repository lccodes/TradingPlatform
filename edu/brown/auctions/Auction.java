package brown.auctions;

import brown.assets.value.Good;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;

public interface Auction {
	/**
	 * Gets the ID of the auction
	 * @return id
	 */
	public Integer getID();
	
	/**
	 * Gets the type of bid bundle required
	 * @return BundleType
	 */
	public BundleType getBundleType();
	
	/**
	 * Gets the BidRequest; this specifies what
	 * agents need to respond to in order to bid
	 * @param ID : agent to tailor the request
	 * @return BidRequest
	 */
	public BidRequest getBidRequest(Integer ID);
	
	/**
	 * Is the auction over
	 * @return true if ended
	 */
	public boolean isOver();
	
	/**
	 * Is the auction private? i.e. will other agents know who the
	 * high bidder is.
	 * @return true if private
	 */
	public boolean isPrivate();
	
	/**
	 * Submit a bid to the auction
	 * @param bid : bid containing agent and bid
	 */
	public void handleBid(Bid bid);
	
	/**
	 * What good is being auctioned?
	 * @return the good being auctioned
	 */
	public Good getGood();
	
	/**
	 * If it's over, who won if anyone?
	 * @nullable
	 * @return who won
	 */
	public BidBundle getWinner();
	
	/**
	 * Tick the auction: always occurs
	 * every cycle (use long to determine time
	 * passage) but only useful for clock auctions
	 * @param time : current mills
	 */
	public void tick(long time);
}
