package brown.auctions.interfaces;

import java.util.List;
import java.util.Set;

import brown.assets.accounting.Order;
import brown.assets.value.ITradeable;
import brown.auctions.bundles.BidBundle;
import brown.messages.auctions.Bid;

public interface MarketInternalState {
	public Integer getID();

	public void addBid(Bid bid);

	public void setAllocation(BidBundle cleanedAlloc);
	
	public void setPayments(List<Order> payments);
	
	public void setReserve(BidBundle o);
	
	public BidBundle getReserve();

	public List<Bid> getBids();

	public Set<ITradeable> getTradeables();

	public BidBundle getAllocation();
	
	public List<Order> getPayments();

	public void tick(long time);

	public int getTicks();

	public void clearBids();

	public double getIncrement();

}
