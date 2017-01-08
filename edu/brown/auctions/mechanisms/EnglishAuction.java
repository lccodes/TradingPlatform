package brown.auctions.mechanisms;

import brown.assets.value.Good;
import brown.auctions.Auction;
import brown.auctions.BundleType;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;

public class EnglishAuction implements Auction {

	@Override
	public Integer getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BundleType getBundleType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BidRequest getBidRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleBid(Bid bid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Good getGood() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getWinner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tick(long time) {
		// TODO Auto-generated method stub
		
	}

}
