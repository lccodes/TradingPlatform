package brown.auctions;

import brown.assets.value.Good;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;

public interface Auction {
	public Integer getID();
	
	public BundleType getBundleType();
	public BidRequest getBidRequest(Integer ID);
	public boolean isOver();
	public boolean isPrivate();
	public void handleBid(Bid bid);
	public Good getGood();
	public BidBundle getWinner();
	public void tick(long time);
}
