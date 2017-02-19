package brown.securities.prediction.structures;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.TransactionOld;
import brown.assets.value.SecurityType;
import brown.securities.MarketCreationException;
import brown.securities.SecurityOld;
import brown.securities.SecurityWrapper;
import brown.securities.mechanisms.lmsr.PMBackend;
import brown.securities.prediction.PredictionMarket;

/**
 * Private backend prediction market implementation
 * Agents are provided pointers to the public face
 * to prevent illegal modifications
 * @author lcamery
 *
 */
public class PMYes implements SecurityOld {
	protected final PMBackend backend;
	protected final Integer ID;
	
	public PMYes() {
		this.ID = null;
		this.backend = null;
	}
	
	public PMYes(Integer id, PMBackend backend) {
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
	 * @param shareNum : int
	 * @return cost : double
	 */
	@Override
	public double bid(double shareNum) {
		return cost(shareNum, 0);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of no shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	@Override
	public double ask(double shareNum) {
		return cost(-1*shareNum, 0);
	}
	
	/*
	 * Returns a transaction to an agent that buys yes
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 * @return share : share object; extendable in real games
	 */
	public List<TransactionOld> buy(Integer agentID, double shareNum, double price) {
		TransactionOld trans = new TransactionOld(this, shareNum, agentID, cost(shareNum, 0), true);
		backend.yes(agentID, shareNum);
		
		List<TransactionOld> ll = new LinkedList<TransactionOld>();
		ll.add(trans);
		return ll;
	}
	
	/*
	 * Returns a transaction to an agent that buys no
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 * @return share : share object; extendable in real games
	 */
	public List<TransactionOld> sell(Integer agentID, double shareNum, double price) {
		TransactionOld trans = new TransactionOld(this, shareNum, agentID, cost(-1 * shareNum, 0), true);
		backend.yes(agentID, -1*shareNum);
		
		List<TransactionOld> ll = new LinkedList<TransactionOld>();
		ll.add(trans);
		return ll;
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
		return SecurityType.PredictionYes;
	}
	
}
