package brown.markets;

import brown.agent.Agent;
import brown.messages.PurchaseRequest;

/**
 * Public accesors to private PM
 * @author lcamery
 *
 */
public class PredictionMarket {
	protected final PM pm;
	
	public PredictionMarket(PM internal) {
		this.pm = internal;
	}
	
	/*
	 * Quotes the cumulative price for a certain number of yes shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double getPriceYes(int shareNum) {
		return pm.getPriceYes(shareNum);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of no shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double getPriceNo(int shareNum) {
		return pm.getPriceNo(shareNum);
	}
	
	/*
	 * Returns a share to an agent that buys yes
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 */
	public void buyYes(Agent agent, int shareNum) {
		PurchaseRequest pr = new PurchaseRequest(agent.ID, pm, shareNum, 0);
		agent.CLIENT.sendTCP(pr);
	}
	
	/*
	 * Returns a share to an agent that buys no
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 */
	public void buyNo(Agent agent, int shareNum) {
		PurchaseRequest pr = new PurchaseRequest(agent.ID, pm, 0, shareNum);
		agent.CLIENT.sendTCP(pr);
	}

}
