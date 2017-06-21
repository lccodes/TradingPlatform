package brown.marketinternalstates;

import java.util.List;
import java.util.Set;

import brown.assets.accounting.Order;
import brown.bundles.BidBundle;
import brown.messages.auctions.Bid;
import brown.tradeables.Tradeable;

public interface MarketInternalState {
	public Integer getID();

	public void addBid(Bid bid);

	public void setAllocation(BidBundle cleanedAlloc);
	
	public void setPayments(List<Order> payments);
	
	public void setReserve(BidBundle o);
	
	public BidBundle getReserve();
	
	public List<Bid> getBids();

	public Set<Tradeable> getTradeables();

	public BidBundle getAllocation();
	
	public List<Order> getPayments();

	public void tick(long time);

	public int getTicks();

	public void clearBids();

	public double getIncrement();

	public void setMaximizingRevenue(boolean b);
	
	public boolean isMaximizingRevenue();

	public int getEligibility();

}
