package brown.securities.prediction;

import java.util.SortedMap;

import brown.agent.Agent;
import brown.assets.accounting.TransactionOld;
import brown.assets.value.SecurityType;
import brown.messages.markets.PurchaseRequest;
import brown.securities.MarketCreationException;
import brown.securities.SecurityOld;
import brown.securities.SecurityWrapper;

/**
 * Public accesors to private PM
 * @author lcamery
 *
 */
public class PredictionMarket implements SecurityWrapper {
	protected final SecurityOld pm;
	
	public PredictionMarket() {
		this.pm = null;
	}
	
	public PredictionMarket(SecurityOld internal) throws MarketCreationException {
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
	public double bid(double shareNum) {
		return pm.bid(shareNum);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of no shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double ask(double shareNum) {
		return pm.ask(shareNum);
	}
	
	/*
	 * Returns a share to an agent that buys yes
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 */
	public void buy(Agent agent, double shareNum) {
		PurchaseRequest pr = new PurchaseRequest(agent.ID, pm, shareNum, 0);
		agent.CLIENT.sendTCP(pr);
	}
	
	/*
	 * Returns a share to an agent that buys no
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 */
	public void sell(Agent agent, double shareNum) {
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

	@Override
	public void buy(Agent agent, double shareNum, double sharePrice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sell(Agent agent, double shareNum, double sharePrice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double bid(double shareNum, double sharePrice) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public double ask(double shareNum, double sharePrice) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public SortedMap<Double, TransactionOld> getBuyOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap<Double, TransactionOld> getSellOrders() {
		// TODO Auto-generated method stub
		return null;
	}

}
