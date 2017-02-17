package brown.auctions;

import java.util.List;
import java.util.SortedMap;

import brown.assets.accounting.SecurityTwo;
import brown.securities.SecurityType;

public interface TwoSidedAuction extends Market {
	public SecurityType getType();
	
	public List<SecurityTwo> buy(Integer agentID, double shareNum, double sharePrice);
	public List<SecurityTwo> sell(Integer agentID, double shareNum, double sharePrice);
	
	public double bid(double shareNum, double sharePrice);
	public double ask(double shareNum, double sharePrice);
	
	public SortedMap<Double, SecurityTwo> getBuyBook();
	public SortedMap<Double, SecurityTwo> getSellBook();
	
	public TwoSidedWrapper wrap();
}
