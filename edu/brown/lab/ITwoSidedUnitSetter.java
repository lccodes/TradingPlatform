package brown.lab;

import java.util.SortedMap;

import brown.agent.Agent;
import brown.auctions.twosided.ITwoSidedWrapper;

public interface ITwoSidedUnitSetter extends ITwoSidedWrapper {
	public void buy(Agent agent, int sharePrice);
	public void sell(Agent agent, int sharePrice);
	public void cancel(Agent agent, boolean buy, int sharePrice);
	
	public int quoteBid(int sharePrice);
	public int quoteAsk(int sharePrice);
	
	public SortedMap<Double, Double> getBuyBook();
	public SortedMap<Double, Double> getSellBook();
}
