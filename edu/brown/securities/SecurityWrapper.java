package brown.securities;

import brown.agent.Agent;

public interface SecurityWrapper {
	
	public Integer getID();
	
	public void buy(Agent agent, int shareNum);
	public void sell(Agent agent, int shareNum);
	
	public double bid(int shareNum);
	public double ask(int shareNum);

}
