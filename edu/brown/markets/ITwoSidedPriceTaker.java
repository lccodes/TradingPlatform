package brown.markets;

import brown.agent.Agent;

public interface ITwoSidedPriceTaker extends ITwoSidedAuction {
	public void buy(Agent agent, double shareNum, double maxPrice);
	public void sell(Agent agent, double shareNum, double maxPrice);
	public void cancel(Agent agent, boolean buy, double shareNum, double maxPrice);
	
	public double quoteBid(double shareNum);
	public double quoteAsk(double shareNum);
}
