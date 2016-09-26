package brown.markets;

import brown.agent.Agent;

public interface Market {
	
	public void buyPositive(Agent agent, int shareNum);
	public void buyNegative(Agent agent, int shareNum);
	
	public void sellPositive(Agent agent, int shareNum);
	public void sellNegative(Agent agent, int shareNum);
	
	public double pricePositive(int shareNum);
	public double priceNegative(int shareNum);
}
