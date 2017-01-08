package brown.auctions.mechanisms;

import brown.assets.value.Good;
import brown.auctions.Auction;
import brown.auctions.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;

public class EnglishAuction implements Auction {
	private final Integer ID;
	private Integer winnerID;
	private final Good GOOD;
	private int currentPrice;
	private int ticksSince;
	
	public EnglishAuction(Integer id, Good good) {
		this.ID = id;
		this.GOOD = good;
		this.winnerID = null;
		this.currentPrice = 0;
		this.ticksSince = 0;
	}
	
	public EnglishAuction(Integer id, Good good, int reserve) {
		this.ID = id;
		this.GOOD = good;
		this.winnerID = null;
		this.currentPrice = reserve;
		this.ticksSince = 0;
	}

	@Override
	public Integer getID() {
		return this.ID;
	}

	@Override
	public BundleType getBundleType() {
		return BundleType.Simple;
	}

	@Override
	public BidRequest getBidRequest() {
		if (this.isOver()) {
			return null;
		}
		return new BidRequest(0, this.ID, this.getBundleType(), this.currentPrice, this.GOOD);
	}

	@Override
	public boolean isOver() {
		return this.ticksSince > 5;
	}

	@Override
	public void handleBid(Bid bid) {
		if (!(bid.Bundle instanceof SimpleBidBundle) || bid.AuctionID != this.ID) {
			return;
		}
		
		SimpleBidBundle bb = (SimpleBidBundle) bid.Bundle;
		if (bb.Bid > this.currentPrice) {
			this.ticksSince = 0;
			this.currentPrice = bb.Bid;
			this.winnerID = bid.AgentID;
		}
	}

	@Override
	public Good getGood() {
		return this.GOOD;
	}

	@Override
	public Integer getWinner() {
		return this.winnerID;
	}

	@Override
	public void tick(long time) {
		this.ticksSince++;
	}

}
