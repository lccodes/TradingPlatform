package brown.securities.prediction.structures;

import brown.assets.accounting.Transaction;
import brown.securities.MarketCreationException;
import brown.securities.Security;
import brown.securities.SecurityType;
import brown.securities.SecurityWrapper;
import brown.securities.prediction.PredictionMarket;

/**
 * Private backend prediction market implementation
 * Agents are provided podoubleers to the public face
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
	public double cost(double newq1, double newq2) {
		return this.backend.cost(newq1, newq2);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of yes shares
	 * @param shareNum : double
	 * @return cost : double
	 */
	@Override
	public double bid(double shareNum) {
		return cost(0, shareNum);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of no shares
	 * @param shareNum : double
	 * @return cost : double
	 */
	@Override
	public double ask(double shareNum) {
		return cost(0, -1 * shareNum);
	}
	
	/*
	 * Returns a transaction to an agent that buys yes
	 * @param agentID : agent's public ID
	 * @param shareNum : double
	 * @return share : share object; extendable in real games
	 */
	public Transaction buy(Integer agentID, double shareNum, double price) {
		Transaction trans = new Transaction(this, shareNum, agentID, cost(0, shareNum));
		backend.no(agentID, shareNum);
		return trans;
	}
	
	/*
	 * Returns a transaction to an agent that buys no
	 * @param agentID : agent's public ID
	 * @param shareNum : double
	 * @return share : share object; extendable in real games
	 */
	public Transaction sell(Integer agentID, double shareNum, double price) {
		Transaction trans = new Transaction(this, shareNum, agentID, cost(0, -1 * shareNum));
		backend.no(agentID, -1*shareNum);
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

	@Override
	public SecurityType getType() {
		return SecurityType.PredicitonNo;
	}
	
}
