package brown.assets.accounting;

import java.util.LinkedList;
import java.util.List;

import brown.assets.value.Good;

public class Account {
	public final Integer ID;
	public final double monies;
	public final List<Good> goods;
	
	/**
	 * Kryo objects require a blank constructor
	 */
	public Account() {
		this.ID = null;
		this.monies = 0;
		this.goods = null;
	}
	
	public Account(Integer ID) {
		this.ID = ID;
		this.monies = new Integer(0);
		this.goods = new LinkedList<Good>();
	}
	
	private Account(Integer ID, double monies, List<Good> Transactions) {
		this.ID = ID;
		this.monies = monies;
		this.goods = Transactions;
	}
	
	public Account addAll(double newMonies, List<Good> newTransactions) {
		if (newTransactions == null) {
			return new Account(this.ID, newMonies+this.monies, goods);
		}
		
		this.goods.addAll(newTransactions);
		return new Account(this.ID, newMonies+this.monies, goods);
	}
	
	public Account add(double newMonies, Good newGood) {
		if (newGood == null) {
			return new Account(this.ID, newMonies+this.monies, goods);
		}
		this.goods.add(newGood);
		return new Account(this.ID, newMonies+this.monies, goods);
	}
	
	public Account remove(double removeMonies, List<Good> removeTransactions) {
		if (removeTransactions == null) {
			return new Account(this.ID, this.monies-removeMonies, this.goods);
		}
		
		this.goods.removeAll(removeTransactions);
		return new Account(this.ID, this.monies-removeMonies, this.goods);
	}
}
