package brown.securities.prediction;

import brown.agent.Agent;
import brown.messages.markets.PurchaseRequest;
import brown.securities.MarketCreationException;
import brown.securities.Security;
import brown.securities.SecurityType;
import brown.securities.SecurityWrapper;

/**
 * Public accesors to private PM
 * @author lcamery
 *
 */
public class PredictionMarket implements SecurityWrapper {
	protected final Security pm;
	
	public PredictionMarket() {
		this.pm = null;
	}
	
	public PredictionMarket(Security internal) throws MarketCreationException {
		if (internal == null) {
			throw new MarketCreationException("Null PM");
		}
		this.pm = internal;
	}
	
	/*
	 * Quotes the cumulative price for a certain number of yes shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double bid(int shareNum) {
		return pm.bid(shareNum);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of no shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double ask(int shareNum) {
		return pm.ask(shareNum);
	}
	
	/*
	 * Returns a share to an agent that buys yes
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 */
	public void buy(Agent agent, int shareNum) {
		PurchaseRequest pr = new PurchaseRequest(agent.ID, pm, shareNum, 0);
		agent.CLIENT.sendTCP(pr);
	}
	
	/*
	 * Returns a share to an agent that buys no
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 */
	public void sell(Agent agent, int shareNum) {
		PurchaseRequest pr = new PurchaseRequest(agent.ID, pm, 0, shareNum);
		agent.CLIENT.sendTCP(pr);
	}

	@Override
	public Integer getID() {
		return pm.getID();
	}

	@Override
	public SecurityType getType() {
		return this.pm.getType();
	}

}
