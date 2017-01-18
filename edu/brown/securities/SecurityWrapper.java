package brown.securities;

import brown.agent.Agent;

public interface SecurityWrapper {
	
	public Integer getID();
	public SecurityType getType();
	
	public void buy(Agent agent, double shareNum);
	public void sell(Agent agent, double shareNum);
	
	public double bid(double shareNum);
	public double ask(double shareNum);

}
