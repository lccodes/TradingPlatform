package brown.assets.accounting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.value.ITradeable;
import brown.auctions.IMarket;

public class Ledger {
	protected final Set<Transaction> transactions;
	protected final Map<ITradeable, Transaction> latest;
	protected final IMarket market;
	
	/**
	 * Constructs a ledger for the given security
	 * @param security : security that all Tradeables will refer to
	 */
	public Ledger(IMarket market) {
		this.market = market;
		this.transactions = new HashSet<Transaction>();
		this.latest = new HashMap<ITradeable, Transaction>();
	}
	
	/**
	 * Adds a transaction
	 * @param t : transaction to add
	 */
	public void add(Transaction t) {
		this.latest.put(t.TRADEABLE,t);
		this.transactions.add(t);
	}
	
	/**
	 * Constructs a set of all transactions
	 * @return set
	 */
	public Set<Transaction> getSet() {
		return this.transactions;
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
		for (Transaction t : trans) {
			this.latest.put(t.TRADEABLE, t);
		}
		this.transactions.addAll(trans);
	}

}
