package brown.assets.accounting;

import java.util.LinkedList;
import java.util.List;

import brown.assets.value.Tradeable;
import brown.auctions.TwoSidedAuction;

public class Ledger {
	protected List<Tradeable> Tradeables;
	protected final TwoSidedAuction tsa;
	
	/**
	 * Constructs a ledger for the given security
	 * @param security : security that all Tradeables will refer to
	 */
	public Ledger(TwoSidedAuction tsa) {
		this.tsa = tsa;
		this.Tradeables = new LinkedList<Tradeable>();
	}
	
	/**
	 * Add a Tradeable
	 * @param t : Tradeable to add
	 */
	public void add(Tradeable t) {
		this.Tradeables.add(t);
	}
	
	/**
	 * Gets a Tradeable
	 * @param i : the index of the Tradeable to get
	 * @return Tradeable i
	 */
	public Tradeable get(int i) {
		return Tradeables.get(i);
	}
	
	/**
	 * Constructs an iterator over the Tradeables
	 * @return iterator
	 */
	public List<Tradeable> getList() {
		return this.Tradeables;
	}

	public void add(List<Tradeable> trans) {
		this.Tradeables.addAll(trans);
	}

}
