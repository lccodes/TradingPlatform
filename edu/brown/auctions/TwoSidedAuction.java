package brown.auctions;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Account;
import brown.assets.accounting.Transaction;
import brown.assets.value.Tradeable;
import brown.securities.SecurityType;

public interface TwoSidedAuction extends Market {
	public SecurityType getType();
	public Account close(Tradeable t);
	
	public List<Transaction> bid(Integer agentID, double shareNum, double sharePrice);
	public List<Transaction> ask(Integer agentID, double shareNum, double sharePrice);
	
	public double quoteBid(double shareNum, double sharePrice);
	public double quoteAsk(double shareNum, double sharePrice);
	
	public SortedMap<Double, Set<Transaction>> getBuyBook();
	public SortedMap<Double, Set<Tradeable>> getSellBook();
	public void fixSellBook(Transaction former, Tradeable toreplace);
	
	public void tick(double time);
	public TwoSidedWrapper wrap();
}
