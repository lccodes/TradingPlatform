package brown.auctions;

import java.util.Map;
import java.util.SortedMap;

import brown.agent.Agent;
import brown.assets.value.SecurityType;

public interface TwoSidedWrapper {
	/**
	 * Gets the ID of the auction
	 * @return id
	 */
	public Integer getID();
	public Map.Entry<SecurityType, Integer> getType();
	
	public void buy(Agent agent, double shareNum, double sharePrice);
	public void sell(Agent agent, double shareNum, double sharePrice);
	
	public double bid(double shareNum, double sharePrice);
	public double ask(double shareNum, double sharePrice);
	
	public SortedMap<Double, Double> getBuyBook();
	public SortedMap<Double, Double> getSellBook();
}
