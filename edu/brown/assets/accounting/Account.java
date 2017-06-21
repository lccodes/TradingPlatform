package brown.assets.accounting;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import brown.tradeables.Tradeable;

public class Account {
	public final Integer ID;
	public final double monies;
	public final List<Tradeable> tradeables;
	
	/**
	 * Kryo objects require a blank constructor
	 */
	public Account() {
		this.ID = null;
		this.monies = 0;
		this.tradeables = null;
	}
	
	/**
	 * Account with the owner's ID; no balance or goods
	 * Use this constructor
	 * @param ID : owner ID
	 */
	public Account(Integer ID) {
		this.ID = ID;
		this.monies = new Integer(0);
		this.tradeables = new LinkedList<Tradeable>();
	}
	
	/**
	 * Constructor with starting balance and goods
	 * @param ID : owner's ID
	 * @param monies : starting monies
	 * @param goods : starting goods
	 */
	private Account(Integer ID, double monies, List<Tradeable> goods) {
		this.ID = ID;
		this.monies = monies;
		if (goods != null) {
			this.tradeables = goods;
		}else{
			this.tradeables = new LinkedList<Tradeable>();
		}
	}
	
	/**
	 * Adds monies and goods; leave 0 or null if not using both
	 * @param newMonies : additional money
	 * @param newGoods : additional goods 
	 * @return updated Account
	 */
	public Account addAll(double newMonies, List<Tradeable> newGoods) {
		if (newGoods == null) {
			return new Account(this.ID, newMonies+this.monies, tradeables);
		}
		
		this.tradeables.addAll(newGoods);
		return new Account(this.ID, newMonies+this.monies, tradeables);
	}
	
	/**
	 * Adds monies and goods; leave 0 or null if not using both
	 * @param newMonies : additional money
	 * @param newGoods : additional good
	 * @return updated Account
	 */
	public Account add(double newMonies, Tradeable newGood) {
		if (newGood == null) {
			return new Account(this.ID, newMonies+this.monies, tradeables);
		}
		this.tradeables.add(newGood);
		return new Account(this.ID, newMonies+this.monies, tradeables);
	}
	
	/**
	 * Adds monies and goods; leave 0 or null if not using both
	 * @param newMonies : additional money
	 * @param newGoods : additional good
	 * @return updated Account
	 */
	public Account add(double newMonies, Set<Tradeable> newGoods) {
		if (newGoods == null) {
			return new Account(this.ID, newMonies+this.monies, tradeables);
		}
		Set<Tradeable> goods = new HashSet<Tradeable>();
		goods.addAll(this.tradeables);
		goods.addAll(newGoods);
		List<Tradeable> unique = new LinkedList<Tradeable>();
		unique.addAll(goods);
		return new Account(this.ID, newMonies+this.monies, unique);
	}
	
	/**
	 * Removes monies and goods; leave 0 or null if not using both
	 * @param newMonies : money to remove
	 * @param newGoods : goods to remove 
	 * @return updated Account
	 */
	public Account remove(double removeMonies, List<Tradeable> removeGoods) {
		if (removeGoods == null) {
			return new Account(this.ID, this.monies-removeMonies, this.tradeables);
		}
		
		this.tradeables.removeAll(removeGoods);
		return new Account(this.ID, this.monies-removeMonies, this.tradeables);
	}

	public Account remove(double removeMonies, Tradeable t) {
		if (t == null) {
			return new Account(this.ID, this.monies-removeMonies, this.tradeables);
		}
		
		List<Tradeable> unique = new LinkedList<Tradeable>();
		for (Tradeable o : this.tradeables) {
			if (!o.equals(t)) {
				unique.add(o);
			}
		}
		return new Account(this.ID, this.monies-removeMonies, unique);
	}

	public Account add(double add) {
		return new Account(this.ID, this.monies+add, this.tradeables);
	}
	
	public Account toAgent() {
		List<Tradeable> forAgent = new LinkedList<Tradeable>();
		for (Tradeable t : this.tradeables) {
			forAgent.add(t.toAgent(this.ID));
		}
		
		return new Account(this.ID, this.monies, forAgent);
	}
	
	@Override
	public String toString () {
		return "(" + this.ID + ": " + this.monies + ", " + this.tradeables + ")"; 
	}
}
