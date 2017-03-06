package brown.assets.accounting;

import brown.assets.value.ITradeable;

public class Transaction {
	public final Integer TO;
	public final ITradeable GOOD;
	public final Integer FROM;
	public final double COST;
	public double QUANTITY;
	
	/**
	 * For Kryo do not use
	 */
	public Transaction() {
		this.TO = null;
		this.FROM = null;
		this.COST = -1;
		this.QUANTITY = -1;
		this.GOOD = null;
	}
	
	/**
	 * Actual order constructor
	 * @param to
	 * @param from
	 * @param cost
	 * @param quantity
	 * @param good
	 */
	public Transaction(Integer to, Integer from, double cost, double quantity, ITradeable good) {
		this.TO = to;
		this.FROM = from;
		this.COST = cost;
		this.QUANTITY = quantity;
		this.GOOD = good;
	}
	
	public void updateQuantity(double quantity) {
		this.QUANTITY = quantity;
	}

}
