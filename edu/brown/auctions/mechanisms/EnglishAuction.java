package brown.auctions.mechanisms;

import brown.assets.value.Good;
import brown.auctions.Auction;
import brown.auctions.BidBundle;
import brown.auctions.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;

public class EnglishAuction implements Auction {
	private final Integer ID;
	private final Good GOOD;
	private final boolean PRIVATE;
	
	private Integer winnerID;
	private double currentPrice;
	private double currentCost;
	private int ticksSince;
	private int br;
	
	public EnglishAuction(Integer id, Good good, boolean priv) {
		this.ID = id;
		this.GOOD = good;
		this.winnerID = null;
		this.currentPrice = 0;
		this.currentCost = 0;
		this.ticksSince = 0;
		this.PRIVATE = priv;
		this.br = 0;
	}
	
	public EnglishAuction(Integer id, Good good, boolean priv, double reserve) {
		this.ID = id;
		this.GOOD = good;
		this.winnerID = null;
		this.currentPrice = reserve;
		this.ticksSince = 0;
		this.currentCost = reserve;
		this.PRIVATE = priv;
		this.br = 0;
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
	public BidRequest getBidRequest(Integer ID) {
		if (this.isOver()) {
			return null;
		}
		return new BidRequest(br++, this.ID, this.getBundleType(), 
				this.currentPrice, this.GOOD, ID == this.winnerID,
				this.PRIVATE ? null : this.winnerID);
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
		if (bb.getCost() > this.currentPrice) {
			this.ticksSince = 0;
			this.currentCost = this.currentPrice;
			this.currentPrice = bb.getCost();
			this.winnerID = bid.AgentID;
		}
	}

	@Override
	public Good getGood() {
		return this.GOOD;
	}

	@Override
	public BidBundle getWinner() {
		return new SimpleBidBundle(this.currentCost, this.winnerID);
	}

	@Override
	public void tick(long time) {
		this.ticksSince++;
	}

	@Override
	public boolean isPrivate() {
		return this.PRIVATE;
	}

}