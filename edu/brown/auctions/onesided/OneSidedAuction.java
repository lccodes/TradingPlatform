package brown.auctions.onesided;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import brown.assets.accounting.Ledger;
import brown.assets.value.ITradeable;
import brown.auctions.IMarket;
import brown.auctions.IMarketWrapper;
import brown.auctions.arules.MechanismType;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.auctions.prules.PaymentType;
import brown.auctions.rules.AllocationRule;
import brown.auctions.rules.PaymentRule;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidReqeust;
import brown.messages.markets.TradeRequest;

public class OneSidedAuction implements IMarket {
	protected final Integer ID;
	protected final AllocationRule ARULE;
	protected final PaymentRule PRULE;
	protected final Set<Bid> BIDS;
	protected final Set<ITradeable> ITEMS;
	
	public OneSidedAuction() {
		this.ID = null;
		this.ARULE = null;
		this.PRULE = null;
		this.BIDS = null;
		this.ITEMS = null;
	}
	
	/**
	 * Constructor
	 * @param ID
	 * @param tradeables
	 * @param allocationRule
	 * @param paymentRule
	 */
	public OneSidedAuction(Integer ID,Set<ITradeable> tradeables, 
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
	 * @return TradeRequest
	 */
	public TradeRequest wrap(Integer ID, Ledger ledger) {
		BidReqeust temp = this.ARULE.getBidRequest(this.BIDS, ID);
		if (temp == null) {
			return null;
		}
		BidBundle toUse = (ID.equals(temp.Current.getAgent()) || !this.isPrivate()) ? 
				temp.Current : temp.Current.wipeAgent(null);
		
		BidReqeust br = new BidReqeust(temp.getID(), this.ID, temp.TYPE, toUse, this.ITEMS);
		IOneSidedWrapper wrapper = null;
		switch(br.TYPE) {
		case Simple:
			wrapper = new SimpleOneSidedWrapper(this.PRULE.getPaymentType(), br, 
					this.ARULE.getAllocationType(),ledger);
			break;
		default:
			break;
		}
		
		return new TradeRequest(0, wrapper, this.ARULE.getAllocationType());
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
	public Map<BidBundle, Set<ITradeable>> getWinners() {
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

	@Override
	public MechanismType getMechanismType() {
		return this.ARULE.getAllocationType();
	}
	
	public PaymentType getPaymentType() {
		return this.PRULE.getPaymentType();
	}

	@Override
	public boolean permitShort() {
		return false;
	}

	@Override
	public IMarketWrapper wrap(Ledger ledger) {
		//TODO: ???
		return null;
	}
}
