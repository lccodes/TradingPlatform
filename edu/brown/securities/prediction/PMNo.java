package brown.securities.prediction;

import brown.assets.accounting.Transaction;
import brown.securities.MarketCreationException;
import brown.securities.Security;
import brown.securities.SecurityWrapper;

/**
 * Private backend prediction market implementation
 * Agents are provided pointers to the public face
 * to prevent illegal modifications
 * @author lcamery
 *
 */
public class PMNo implements Security {
	protected final PMBackend backend;
	protected final Integer ID;
	
	public PMNo() {
		this.ID = null;
		this.backend = null;
	}
	
	public PMNo(Integer id, PMBackend backend) {
		this.ID = id;
		this.backend = backend;
	}
	
	/*
	 * Invokes backend cost
	 */
	@Override
	public double cost(int newq1, int newq2) {
		return this.backend.cost(newq1, newq2);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of yes shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	@Override
	public double bid(int shareNum) {
		return cost(0, shareNum);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of no shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	@Override
	public double ask(int shareNum) {
		return cost(0, -1 * shareNum);
	}
	
	/*
	 * Returns a transaction to an agent that buys yes
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 * @return share : share object; extendable in real games
	 */
	public Transaction buy(Integer agentID, int shareNum) {
		Transaction trans = new Transaction(this, shareNum, agentID, cost(0, shareNum));
		backend.no(shareNum);
		return trans;
	}
	
	/*
	 * Returns a transaction to an agent that buys no
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 * @return share : share object; extendable in real games
	 */
	public Transaction sell(Integer agentID, int shareNum) {
		Transaction trans = new Transaction(this, shareNum, agentID, cost(0, -1 * shareNum));
		backend.no(-1*shareNum);
		return trans;
	}
	
	public SecurityWrapper wrap() {
		try {
			return new PredictionMarket(this);
		} catch (MarketCreationException e) {
			System.out.println("[x] error making market");
		}
		
		return null;
	}
	
	public Integer getID() {
		return this.ID;
	}
	
}
