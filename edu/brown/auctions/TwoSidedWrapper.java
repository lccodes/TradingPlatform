package brown.auctions;

import java.util.SortedMap;

import brown.agent.Agent;
import brown.assets.accounting.SecurityTwo;
import brown.securities.SecurityType;

public interface TwoSidedWrapper {
	/**
	 * Gets the ID of the auction
	 * @return id
	 */
	public Integer getID();
	public SecurityType getType();
	
	public void buy(Agent agent, double shareNum, double sharePrice);
	public void sell(Agent agent, double shareNum, double sharePrice);
	
	public double bid(double shareNum, double sharePrice);
	public double ask(double shareNum, double sharePrice);
	
	public SortedMap<Double, SecurityTwo> getBuyBook();
	public SortedMap<Double, SecurityTwo> getSellBook();
}
