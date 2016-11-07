package brown.securities;

import brown.assets.accounting.Transaction;

public interface Security {
	
	public Integer getID();
	public SecurityType getType();
	
	public Transaction buy(Integer agentID, int shareNum);
	public Transaction sell(Integer agentID, int shareNum);
	
	public double cost(int newq1, int newq2);
	
	public double bid(int shareNum);
	public double ask(int shareNum);
	
	public SecurityWrapper wrap();
}
