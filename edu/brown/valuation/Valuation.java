package brown.valuation; 

import java.util.Set;

import brown.assets.value.FullType;

public abstract class Valuation {
	
	private Set<FullType> goods;
	private Double price;
	
	public Valuation(Set<FullType> goods, Double price) {
		this.goods = goods; 
		this.price = price; 
	}
	
	public Set<FullType> getGoods() {
		return goods;
	}
	
	public Double getPrice() {
		return price; 
	}
	
	public void setPrice(Double newPrice) {
		price = newPrice; 
	}
	
	
}