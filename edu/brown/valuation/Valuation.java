package brown.valuation; 

import java.util.AbstractMap.SimpleEntry;
import java.util.Set;

import brown.assets.value.FullType;

public class Valuation {
	
	private SimpleEntry<Set<FullType>, Double> entry;
	
	public Valuation(Set<FullType> goods, Double price) {
		this.entry = new SimpleEntry<Set<FullType>, Double>(goods, price);
	}
	
	public Set<FullType> getGoods() {
		return entry.getKey(); 
	}
	
	public Double getPrice() {
		return entry.getValue(); 
	}
	
	public void setPrice(Double newPrice) {
		entry.setValue(newPrice);
	}

	@Override
	public String toString() {
		return "Valuation [entry=" + entry + "]";
	}
	
	
}