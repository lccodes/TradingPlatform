package brown.securities;

import java.util.SortedMap;

import brown.agent.Agent;
import brown.assets.accounting.Transaction;

public interface SecurityWrapper {
	
	public Integer getID();
	public SecurityType getType();
	
	public void buy(Agent agent, double shareNum, double sharePrice);
	public void sell(Agent agent, double shareNum, double sharePrice);
	
	public void buy(Agent agent, double shareNum);
	public void sell(Agent agent, double shareNum);
	
	public double bid(double shareNum, double sharePrice);
	public double ask(double shareNum, double sharePrice);
	
	public double bid(double shareNum);
	public double ask(double shareNum);
	
	public SortedMap<Double, Transaction> getBuyOrders();
	public SortedMap<Double, Transaction> getSellOrders();

}
