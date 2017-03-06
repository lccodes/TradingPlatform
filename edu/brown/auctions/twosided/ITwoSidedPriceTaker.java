package brown.auctions.twosided;

import brown.agent.Agent;

public interface ITwoSidedPriceTaker {
	public void buy(Agent agent, double shareNum, double maxPrice);
	public void sell(Agent agent, double shareNum, double maxPrice);
	
	public double quoteBid(double shareNum);
	public double quoteAsk(double shareNum);
}
