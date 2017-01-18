package brown.securities;

import brown.assets.accounting.Transaction;

public interface Security {
	
	public Integer getID();
	public SecurityType getType();
	
	public Transaction buy(Integer agentID, double shareNum);
	public Transaction sell(Integer agentID, double shareNum);
	
	public double cost(double newq1, double newq2);
	
	public double bid(double shareNum);
	public double ask(double shareNum);
	
	public SecurityWrapper wrap();
}
