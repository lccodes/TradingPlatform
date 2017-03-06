package brown.assets.accounting;

import brown.assets.value.ITradeable;

public class Transaction {
	public final Integer TO;
	public final ITradeable TRADEABLE;
	public final Integer FROM;
	public final double PRICE;
	public final double QUANTITY;
	
	public final long TIMESTAMP;
	
	/**
	 * For Kryo do not use
	 */
	public Transaction() {
		this.TO = null;
		this.FROM = null;
		this.PRICE = -1;
		this.QUANTITY = -1;
		this.TIMESTAMP = 0;
		this.TRADEABLE = null;
	}
	
	/**
	 * Actual transaction constructor
	 * @param to
	 * @param from
	 * @param cost
	 * @param quantity
	 * @param TRADEABLE
	 */
	public Transaction(Integer to, Integer from, double price, double quantity, ITradeable TRADEABLE) {
		this.TO = to;
		this.FROM = from;
		this.PRICE = price;
		this.QUANTITY = quantity;
		this.TRADEABLE = TRADEABLE;
		this.TIMESTAMP = System.currentTimeMillis();
	}

}
