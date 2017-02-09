package brown.securities;

import java.util.List;

import brown.assets.accounting.Transaction;

public interface Security {
	
	public Integer getID();
	public SecurityType getType();
	
	public List<Transaction> buy(Integer agentID, double shareNum, double sharePrice);
	public List<Transaction> sell(Integer agentID, double shareNum, double sharePrice);
	
	public double cost(double newq1, double newq2);
	
	public double bid(double shareNum);
	public double ask(double shareNum);
	
	public SecurityWrapper wrap();
}
