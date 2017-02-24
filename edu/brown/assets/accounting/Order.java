package brown.assets.accounting;

import brown.assets.value.Tradeable;

public class Order {
	public final Integer TO;
	public final Tradeable GOOD;
	public final Integer FROM;
	public final double COST;
	public double QUANTITY;
	
	public Order(Integer to, Integer from, double cost, double quantity, Tradeable good) {
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
