package brown.assets.accounting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.markets.Market;
import brown.tradeables.Tradeable;

/**
 * A ledger tracks all trades within a market. 
 * @author lcamery
 *
 */
public class Ledger {
	protected final List<Transaction> transactions;
	protected final Map<Tradeable, Transaction> latest;
	protected final List<Transaction> unshared;
	protected final Market market;
	
	/**
	 * For Kryo do not use
	 */
	public Ledger() {
		this.transactions = null;
		this.latest = null;
		this.market = null;
		this.unshared = null;
	}
	
	/**
	 * Constructs a ledger for the given security
	 * @param security : security that all Tradeables will refer to
	 */
	public Ledger(Market market) {
		this.market = market;
		this.unshared = new LinkedList<Transaction>();
		this.transactions = new LinkedList<Transaction>();
		this.latest = new HashMap<Tradeable, Transaction>();
	}
	
	/**
	 * Adds a transaction
	 * @param t : transaction to add
	 */
	public void add(Transaction t) {
		synchronized(transactions) {
			this.latest.put(t.TRADEABLE,t);
			this.transactions.add(t);
			this.unshared.add(t);
		}
	}
	
	/**
	 * Constructs a set of all transactions
	 * @return set
	 */
	public List<Transaction> getList() {
		return new LinkedList<Transaction>(this.transactions);
	}
	
	/**
	 * Gets the latest transactions
	 * @return
	 */
	public Set<Transaction> getLatest() {
		return new HashSet<Transaction>(this.latest.values());
	}

	/**
	 * Adds a list of transactions
	 * @param trans
	 */
	public void add(List<Transaction> trans) {
		synchronized(transactions) {
			for (Transaction t : trans) {
				this.latest.put(t.TRADEABLE, t);
			}
			this.transactions.addAll(trans);
			this.unshared.addAll(trans);
		}
	}
	
	/**
	 * Gets the ledger without others' IDs
	 * @param ID : this agent's ID
	 * @return ledger
	 */
	public Ledger getSanitized(Integer ID) {
		Ledger ledger = new Ledger(null);
		synchronized(transactions) {
			for (Transaction t : this.unshared) {
				ledger.add(t.sanitize(ID));
			}
		}
		return ledger;
	}
	
	/**
	 * Clears the latest set
	 */
	public void clearLatest() {
		this.unshared.clear();
	}

}
