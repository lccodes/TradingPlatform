package brown.auctions.rules;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Order;
import brown.assets.value.ITradeable;

public interface ClearingRule {
	public List<Order> buy(Integer agentID, double shareNum, double sharePrice);
	public List<Order> sell(Integer agentID, ITradeable opp, double sharePrice);
	
	public double quoteBid(double shareNum, double sharePrice);
	public double quoteAsk(double shareNum, double sharePrice);
	public double price();
	
	public SortedMap<Double, Set<Order>> getBuyBook();
	public SortedMap<Double, Set<Order>> getSellBook();
	
	public void tick(double time);
	public boolean isShort();
	public void cancel(Integer agentID, boolean buy, double shareNum,
			double sharePrice);
}
