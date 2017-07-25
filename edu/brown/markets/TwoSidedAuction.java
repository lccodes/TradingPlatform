package brown.markets;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Order;
import brown.assets.value.BasicType;
import brown.tradeables.Tradeable;

public interface TwoSidedAuction extends IMarketServer {
	public BasicType getTradeableType();
	
	public List<Order> buy(Integer agentID, double shareNum, double sharePrice);
	public List<Order> sell(Integer agentID, Tradeable opp, double sharePrice);
	public void cancel(Integer agentID, boolean buy, double shareNum, double sharePrice);
	
	public double quoteBid(double shareNum, double sharePrice);
	public double quoteAsk(double shareNum, double sharePrice);
	
	public SortedMap<Double, Set<Order>> getBuyBook();
	public SortedMap<Double, Set<Order>> getSellBook();
	
	public void tick(double time);
}
