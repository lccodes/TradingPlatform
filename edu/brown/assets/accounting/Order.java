package brown.assets.accounting;

import brown.assets.value.ITradeable;

public class Order {
	public final Integer TO;
	public final ITradeable GOOD;
	public final Integer FROM;
	public final double COST;
	public double QUANTITY;
	
	/**
	 * For Kryo do not use
	 */
	public Order() {
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
	public Order(Integer to, Integer from, double cost, double quantity, ITradeable good) {
		this.TO = to;
		this.FROM = from;
		this.COST = cost;
		this.QUANTITY = quantity;
		this.GOOD = good;
	}
	
	public void updateQuantity(double quantity) {
		this.QUANTITY = quantity;
	}

	public Transaction toTransaction() {
		return new Transaction(this.TO,this.FROM,this.COST/this.QUANTITY,this.QUANTITY,this.GOOD);
	}

	public Order updatePrice(double cost) {
		return new Order(this.TO, this.FROM, cost, this.QUANTITY, this.GOOD);
	}

	public Order updateToAgent(Integer newAgent) {
		return new Order(newAgent, this.FROM, this.COST, this.QUANTITY, this.GOOD);
	}
	
	@Override
	public String toString() {
		return "<" + this.TO + "," + this.COST + ">";
	}

}
