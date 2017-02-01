package brown.auctions.mechanisms;

import java.util.HashSet;
import java.util.Set;

import brown.assets.value.Good;
import brown.auctions.Auction;
import brown.auctions.BidBundle;
import brown.auctions.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;

public class SealedBid implements Auction {
	private final Integer ID;
	private final Good Good;
	private final boolean ASCENDING;
	private final boolean FIRST;
	private final Set<Integer> alreadyGotten;
	private final double RESERVE;
	
	private double ticksSince;
	private Integer winnerID;
	private double currentPrice;
	private double currentCost;
	private int br;
	
	/**
	 * Sealed bid auction without reserve
	 * @param ID : auction ID
	 * @param good : good being auctioned
	 * @param ascending : is it ascending?
	 * @param firstPrice : is it first price? otherwise second price
	 */
	public SealedBid(Integer ID, Good good, boolean ascending, boolean firstPrice) {
		this.ID = ID;
		this.Good = good;
		this.ticksSince = 0;
		this.winnerID = null;
		this.currentCost = ascending ? 0 : Double.MAX_VALUE;
		this.currentPrice = ascending ? 0 : Double.MAX_VALUE;
		this.RESERVE = ascending ? 0 : Double.MAX_VALUE;
		this.br = 0;
		this.ASCENDING = ascending;
		this.FIRST = firstPrice;
		this.alreadyGotten = new HashSet<Integer>();
	}
	
	/**
	 * Sealed bid auction with reserve
	 * @param ID : auction ID
	 * @param good : good being auctioned
	 * @param ascending : is it ascending?
	 * @param firstPrice : is it first price? otherwise second price
	 * @param reserve : reserve price
	 */
	public SealedBid(Integer ID, Good good, boolean ascending,
			boolean firstPrice, double reserve) {
		this.ID = ID;
		this.Good = good;
		this.ticksSince = 0;
		this.winnerID = null;
		this.currentCost = reserve;
		this.currentPrice = reserve;
		this.RESERVE = reserve;
		this.br = 0;
		this.ASCENDING = ascending;
		this.FIRST = firstPrice;
		this.alreadyGotten = new HashSet<Integer>();
	}

	@Override
	public Integer getID() {
		return this.ID;
	}

	@Override
	public BundleType getBundleType() {
		return BundleType.SimpleSealed;
	}

	@Override
	public BidRequest getBidRequest(Integer ID) {
		if (this.isOver() || this.alreadyGotten.contains(ID)) {
			return null;
		}
		
		return new BidRequest(br++,this.ID,this.getBundleType(),this.RESERVE, this.Good,false,null);
	}

	@Override
	public boolean isOver() {
		return this.ticksSince > 5;
	}

	@Override
	public boolean isPrivate() {
		return true;
	}

	@Override
	public void handleBid(Bid bid) {
		if (!(bid.Bundle instanceof SimpleBidBundle) || bid.AuctionID != this.ID) {
			return;
		}
		
		SimpleBidBundle bb = (SimpleBidBundle) bid.Bundle;
		if ((this.ASCENDING && bb.getCost() > this.currentPrice) || bb.getCost() < this.currentPrice) {
			this.ticksSince = 0;
			this.currentCost = this.FIRST ? bb.getCost() : this.currentPrice;
			this.currentPrice = bb.getCost();
			this.winnerID = bid.AgentID;
		}
		this.alreadyGotten.add(bid.AgentID);
	}

	@Override
	public Good getGood() {
		return this.Good;
	}

	@Override
	public BidBundle getWinner() {
		return new SimpleBidBundle(this.currentCost, this.winnerID);
	}

	@Override
	public void tick(long time) {
		this.ticksSince++;
	}

}
