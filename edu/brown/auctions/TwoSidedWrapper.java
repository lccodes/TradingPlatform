package brown.auctions;

import java.util.SortedMap;

import brown.agent.Agent;
import brown.assets.value.FullType;

public interface TwoSidedWrapper {
	/**
	 * Gets the ID of the auction
	 * @return id
	 */
	public Integer getID();
	public FullType getType();
	
	public void buy(Agent agent, double shareNum, double sharePrice);
	public void sell(Agent agent, double shareNum, double sharePrice);
	
	public double quoteBid(double shareNum, double sharePrice);
	public double quoteAsk(double shareNum, double sharePrice);
	
	public SortedMap<Double, Double> getBuyBook();
	public SortedMap<Double, Double> getSellBook();
}
