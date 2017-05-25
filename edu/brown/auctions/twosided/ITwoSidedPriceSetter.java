package brown.auctions.twosided;

import java.util.SortedMap;

import brown.agent.Agent;

public interface ITwoSidedPriceSetter extends ITwoSidedWrapper {
	public void buy(Agent agent, double shareNum, double sharePrice);
	public void sell(Agent agent, double shareNum, double sharePrice);
	public void cancel(Agent agent, boolean buy, double shareNum, double sharePrice);
	
	public double quoteBid(double shareNum, double sharePrice);
	public double quoteAsk(double shareNum, double sharePrice);
	
	public SortedMap<Double, Double> getBuyBook();
	public SortedMap<Double, Double> getSellBook();
}
