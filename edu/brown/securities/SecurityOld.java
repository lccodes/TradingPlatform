package brown.securities;

import java.util.List;

import brown.assets.accounting.TransactionOld;

public interface SecurityOld {
	
	public Integer getID();
	public SecurityType getType();
	
	public List<TransactionOld> buy(Integer agentID, double shareNum, double sharePrice);
	public List<TransactionOld> sell(Integer agentID, double shareNum, double sharePrice);
	
	public double cost(double newq1, double newq2);
	
	public double bid(double shareNum);
	public double ask(double shareNum);
	
	public SecurityWrapper wrap();
}
