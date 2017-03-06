package brown.assets.accounting;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import brown.assets.value.ITradeable;
import brown.auctions.twosided.TwoSidedAuction;

public class Ledger {
	protected final Set<ITradeable> tradeables;
	protected final TwoSidedAuction tsa;
	
	/**
	 * Constructs a ledger for the given security
	 * @param security : security that all Tradeables will refer to
	 */
	public Ledger(TwoSidedAuction tsa) {
		this.tsa = tsa;
		this.tradeables = new HashSet<ITradeable>();
	}
	
	/**
	 * Add a Tradeable
	 * @param t : Tradeable to add
	 */
	public void add(ITradeable t) {
		this.tradeables.add(t);
	}
	
	/**
	 * Constructs a set of the Tradeables
	 * @return set
	 */
	public Set<ITradeable> getSet() {
		return this.tradeables;
	}

	/**
	 * Adds a list of tradeables
	 * @param trans
	 */
	public void add(List<ITradeable> trans) {
		this.tradeables.addAll(trans);
	}

}
