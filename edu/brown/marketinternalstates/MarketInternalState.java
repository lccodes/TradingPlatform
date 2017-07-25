package brown.marketinternalstates;

import java.util.List;
import java.util.Set;

import brown.assets.accounting.Order;
import brown.bundles.BidBundle;
import brown.messages.auctions.Bid;
import brown.tradeables.Tradeable;

/**
 * this interface stores the internal state of a market as 
 * bidding is occurring.
 * @author acoggins
 *
 */
public interface MarketInternalState {
  /**
   * returns the auction id
   * @return
   */
	public Integer getID();

	/**
	 * adds a bid 
	 * @param bid
	 */
	public void addBid(Bid bid);

	/**
	 * sets the allocation of the auction.
	 * @param cleanedAlloc
	 */
	public void setAllocation(BidBundle cleanedAlloc);
	
	/**
	 * sets the payments of the auction.
	 * @param payments
	 */
	public void setPayments(List<Order> payments);
	
	/**
	 * sets the reserve of the auction.
	 * @param reserveBundle
	 */
	public void setReserve(BidBundle reserveBundle);
	
	/**
	 * gets reserve bundle from internal state.
	 * @return
	 * reserve bundle.
	 */
	public BidBundle getReserve();
	
	/**
	 * gets and returns bids from internal state.
	 * @return
	 */
	public List<Bid> getBids();

	/**
	 * gets and returns tradeables from internal state.
	 * @return
	 */
	public Set<Tradeable> getTradeables();

	/**
	 * gets and returns allocation bundle from internal state.
	 * @return
	 */
	public BidBundle getAllocation();
	
	/**
	 * gets and returns payments from internal state.
	 * @return
	 */
	public List<Order> getPayments();

	/**
	 * increments time.
	 * @param time
	 */
	public void tick(long time);

	/**
	 * returns number of ticks from internal state.
	 * @return
	 */
	public int getTicks();

	/**
	 * clears all bids from internal state.
	 */
	public void clearBids();

	/**
	 * returns increment stored in internal state.
	 * @return
	 */
	public double getIncrement();

	/**
	 * sets maximizing revenue in internal state.
	 * @param b
	 */
	public void setMaximizingRevenue(boolean b);
	
	/**
	 * returns whether or not there is maximizing revenue.
	 * @return
	 */
	public boolean isMaximizingRevenue();

	/**
	 * returns the number of bids that have been placed. 
	 * @return
	 */
	public int getEligibility();

}
