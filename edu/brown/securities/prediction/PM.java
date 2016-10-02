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
public abstract class PM implements Security {
	protected double yes;
	protected double no;
	protected final double b;
	public final Integer ID;
	
	public PM() {
		this.ID = null;
		this.yes = 0;
		this.no = 0;
		this.b = 1;
	}
	
	public PM(Integer id, double b) {
		this.ID = id;
		this.yes = 0;
		this.no = 0;
		this.b = b;
	}
	
	/*
	 * Cost function
	 * @param qd1 : new quantity yes
	 * @param qd2 : new quantity no
	 * @return cost : double
	 */
	public double cost(int newq1, int newq2) {
		return b*Math.log(Math.pow(Math.E, (newq1 + yes)/b) + Math.pow(Math.E, (newq2+no)/b))
				- b*Math.log(Math.pow(Math.E, yes/b) + Math.pow(Math.E, no/b));
	}
	
	/*
	 * Quotes the cumulative price for a certain number of yes shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double pricePositive(int shareNum) {
		return cost(shareNum, 0);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of no shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double priceNegative(int shareNum) {
		return cost(0, shareNum);
	}
	
	/*
	 * Returns a share to an agent that buys yes
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 * @return share : share object; extendable in real games
	 */
	public abstract Transaction buy(Integer agentID, int shareNum);
	
	/*
	 * Returns a share to an agent that buys no
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 * @return share : share object; extendable in real games
	 */
	public abstract Transaction sell(Integer agentID, int shareNum);
	
	public SecurityWrapper wrap() {
		try {
			return new PredictionMarket(this);
		} catch (MarketCreationException e) {
			System.out.println("[x] error making market");
		}
		
		return null;
	}
	
}
