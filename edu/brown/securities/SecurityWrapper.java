package brown.securities;

import brown.agent.Agent;

public interface SecurityWrapper {
	
	public void buyYes(Agent agent, int shareNum);
	public void buyNo(Agent agent, int shareNum);
	
	public double getPriceYes(int shareNum);
	public double getPriceNo(int shareNum);

}
