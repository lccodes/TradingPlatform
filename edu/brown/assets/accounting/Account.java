package brown.assets.accounting;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import brown.assets.value.ITradeable;

public class Account {
	public final Integer ID;
	public final double monies;
	public final List<ITradeable> goods;
	
	/**
	 * Kryo objects require a blank constructor
	 */
	public Account() {
		this.ID = null;
		this.monies = 0;
		this.goods = null;
	}
	
	/**
	 * Account with the owner's ID; no balance or goods
	 * Use this constructor
	 * @param ID : owner ID
	 */
	public Account(Integer ID) {
		this.ID = ID;
		this.monies = new Integer(0);
		this.goods = new LinkedList<ITradeable>();
	}
	
	/**
	 * Constructor with starting balance and goods
	 * @param ID : owner's ID
	 * @param monies : starting monies
	 * @param goods : starting goods
	 */
	private Account(Integer ID, double monies, List<ITradeable> goods) {
		this.ID = ID;
		this.monies = monies;
		this.goods = goods;
	}
	
	/**
	 * Adds monies and goods; leave 0 or null if not using both
	 * @param newMonies : additional money
	 * @param newGoods : additional goods 
	 * @return updated Account
	 */
	public Account addAll(double newMonies, List<ITradeable> newGoods) {
		if (newGoods == null) {
			return new Account(this.ID, newMonies+this.monies, goods);
		}
		
		this.goods.addAll(newGoods);
		return new Account(this.ID, newMonies+this.monies, goods);
	}
	
	/**
	 * Adds monies and goods; leave 0 or null if not using both
	 * @param newMonies : additional money
	 * @param newGoods : additional good
	 * @return updated Account
	 */
	public Account add(double newMonies, ITradeable newGood) {
		if (newGood == null) {
			return new Account(this.ID, newMonies+this.monies, goods);
		}
		this.goods.add(newGood);
		return new Account(this.ID, newMonies+this.monies, goods);
	}
	
	/**
	 * Adds monies and goods; leave 0 or null if not using both
	 * @param newMonies : additional money
	 * @param newGoods : additional good
	 * @return updated Account
	 */
	public Account add(double newMonies, Set<ITradeable> newGoods) {
		if (newGoods == null) {
			return new Account(this.ID, newMonies+this.monies, goods);
		}
		Set<ITradeable> goods = new HashSet<ITradeable>();
		goods.addAll(this.goods);
		goods.addAll(newGoods);
		List<ITradeable> unique = new LinkedList<ITradeable>();
		unique.addAll(goods);
		return new Account(this.ID, newMonies+this.monies, unique);
	}
	
	/**
	 * Removes monies and goods; leave 0 or null if not using both
	 * @param newMonies : money to remove
	 * @param newGoods : goods to remove 
	 * @return updated Account
	 */
	public Account remove(double removeMonies, List<ITradeable> removeGoods) {
		if (removeGoods == null) {
			return new Account(this.ID, this.monies-removeMonies, this.goods);
		}
		
		this.goods.removeAll(removeGoods);
		return new Account(this.ID, this.monies-removeMonies, this.goods);
	}

	public Account remove(double removeMonies, ITradeable t) {
		if (t == null) {
			return new Account(this.ID, this.monies-removeMonies, this.goods);
		}
		
		List<ITradeable> unique = new LinkedList<ITradeable>();
		for (ITradeable o : this.goods) {
			if (!o.equals(t)) {
				unique.add(o);
			}
		}
		return new Account(this.ID, this.monies-removeMonies, unique);
	}

	public Account add(double add) {
		return new Account(this.ID, this.monies+add, this.goods);
	}
	
	public Account toAgent() {
		List<ITradeable> forAgent = new LinkedList<ITradeable>();
		for (ITradeable t : this.goods) {
			forAgent.add(t.toAgent());
		}
		
		return new Account(this.ID, this.monies, forAgent);
	}
}
