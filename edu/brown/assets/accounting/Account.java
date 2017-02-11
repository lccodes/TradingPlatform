package brown.assets.accounting;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import brown.assets.value.Tradeable;

public class Account {
	public final Integer ID;
	public final double monies;
	public final List<Tradeable> goods;
	
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
		this.goods = new LinkedList<Tradeable>();
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
		this.goods = goods;
	}
	
	/**
	 * Adds monies and goods; leave 0 or null if not using both
	 * @param newMonies : additional money
	 * @param newGoods : additional goods 
	 * @return updated Account
	 */
	public Account addAll(double newMonies, List<Tradeable> newGoods) {
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
	public Account add(double newMonies, Tradeable newGood) {
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
	public Account add(double newMonies, Set<Tradeable> newGoods) {
		if (newGoods == null) {
			return new Account(this.ID, newMonies+this.monies, goods);
		}
		this.goods.addAll(newGoods);
		return new Account(this.ID, newMonies+this.monies, goods);
	}
	
	/**
	 * Removes monies and goods; leave 0 or null if not using both
	 * @param newMonies : money to remove
	 * @param newGoods : goods to remove 
	 * @return updated Account
	 */
	public Account remove(double removeMonies, List<Tradeable> removeGoods) {
		if (removeGoods == null) {
			return new Account(this.ID, this.monies-removeMonies, this.goods);
		}
		
		this.goods.removeAll(removeGoods);
		return new Account(this.ID, this.monies-removeMonies, this.goods);
	}
}
