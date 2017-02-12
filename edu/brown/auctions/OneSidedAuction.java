package brown.auctions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import brown.assets.value.Tradeable;
import brown.auctions.rules.AllocationRule;
import brown.auctions.rules.PaymentRule;
import brown.messages.auctions.Bid;
import brown.messages.auctions.TradeRequest;

public class OneSidedAuction implements Market {
	protected final Integer ID;
	protected final AllocationRule ARULE;
	protected final PaymentRule PRULE;
	protected final Set<Bid> BIDS;
	protected final Set<Tradeable> ITEMS;
	
	public OneSidedAuction(Integer ID,Set<Tradeable> tradeables, 
			AllocationRule allocationRule, PaymentRule paymentRule) {
		//TODO: Confirm allocation and payment rules use same bundle
		this.ID = ID;
		this.ARULE = allocationRule;
		this.PRULE = paymentRule;
		this.BIDS = new HashSet<Bid>();
		this.ITEMS = tradeables;
	}
	
	/**
	 * Gets the type of bid bundle required
	 * @return BundleType
	 */
	public BundleType getBundleType() {
		return this.ARULE.getBundleType();
	}
	
	/**
	 * Gets the BidRequest; this specifies what
	 * agents need to respond to in order to bid
	 * @param ID : agent to tailor the request
	 * @return BidRequest
	 */
	public TradeRequest getBidRequest(Integer ID) {
		TradeRequest temp = this.ARULE.getBidRequest(this.BIDS, ID);
		if (temp == null) {
			return null;
		}
		BidBundle toUse = (ID.equals(temp.Current.getAgent()) || !this.isPrivate()) ? 
				temp.Current : temp.Current.wipeAgent(null);
		
		return new TradeRequest(temp.getID(), this.ID, temp.BundleType, toUse, this.ITEMS);
	}
	
	/**
	 * Is the auction private? i.e. will other agents know who the
	 * high bidder is.
	 * @return true if private
	 */
	public boolean isPrivate() {
		return this.ARULE.isPrivate();
	}
	
	/**
	 * Submit a bid to the auction
	 * @param bid : bid containing agent and bid
	 */
	public void handleBid(Bid bid) {
		if (bid.Bundle.getType() != this.ARULE.getBundleType() 
				|| bid.AuctionID != this.ID
				|| !this.ARULE.isValid(bid, this.BIDS)) {
			return;
		}
		
		this.BIDS.add(bid);
		this.ARULE.tick(-1);
	}
	
	/**
	 * If it's over, who won if anyone?
	 * @return who won
	 */
	public Map<BidBundle, Set<Tradeable>> getWinners() {
		return this.PRULE.getPayments(this.ARULE.getAllocations(this.BIDS, this.ITEMS), 
				this.ARULE.withReserve(this.BIDS));
	}
	
	/**
	 * Tick the auction: always occurs
	 * every cycle (use long to determine time
	 * passage) but only useful for clock auctions
	 * @param time : current mills
	 */
	public void tick(long time) {
		this.ARULE.tick(time);
	}
	
	/**
	 * Gets the ID of the auction
	 * @return id
	 */
	public Integer getID() {
		return this.ID;
	}
	
	/**
	 * Is the market closed
	 * @return true if ended
	 */
	public boolean isClosed() {
		return this.ARULE.isOver();
	}
}
