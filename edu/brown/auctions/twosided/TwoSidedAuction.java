package brown.auctions.twosided;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Order;
import brown.assets.value.ITradeable;
import brown.auctions.IMarket;

public interface TwoSidedAuction extends IMarket {
	public ITradeable getTradeable();
	
	public List<Order> buy(Integer agentID, double shareNum, double sharePrice);
	public List<Order> sell(Integer agentID, ITradeable opp, double sharePrice);
	
	public double quoteBid(double shareNum, double sharePrice);
	public double quoteAsk(double shareNum, double sharePrice);
	
	public SortedMap<Double, Set<Order>> getBuyBook();
	public SortedMap<Double, Set<Order>> getSellBook();
	
	public void tick(double time);
	public ITwoSidedWrapper wrap();
}
