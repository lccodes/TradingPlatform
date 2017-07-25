package brown.valuation; 

import java.util.AbstractMap.SimpleEntry;
import java.util.Set;

import brown.assets.value.BasicType;

//TODO: Different datatype for single valuation? Or overload this one.
/**
 * a valuation maps a price to a good or a set of goods.
 * @author acoggins
 *
 */
public class Valuation {
	
	private SimpleEntry<Set<BasicType>, Double> entry;
	
	
	public Valuation() {
	  
	}
	
	/**
	 * constructor for a valuation over a set of goods.
	 * @param goods
	 * a set of fulltype. 
	 * @param price
	 * a price for this set.
	 */
	public Valuation(Set<BasicType> goods, Double price) {
		this.entry = new SimpleEntry<Set<BasicType>, Double>(goods, price);
	}
	
	/**
	 * gets the goods associated with this valuation. 
	 * @return
	 * a set of FullType.
	 */
	public Set<BasicType> getGoods() {
		return entry.getKey();
	}
	
	/**
	 * get the price for a valuation.
	 * @return
	 * Double representing a price.
	 */
	public Double getPrice() {
		return entry.getValue(); 
	}
	
	/**
	 * set the price for a valuation
	 * @param newPrice
	 */
	public void setPrice(Double newPrice) {
	    entry.setValue(newPrice);
	}
	
	/**
	 * does the valuation contain the good?
	 * @param good
	 * @return
	 * the FullType good.
	 */
	public Boolean contains(BasicType good) {
	  return this.getGoods().contains(good);
	}
	
	/**
	 * returns the number of goods being valued.
	 * @return
	 * the number of goods being valued in this valuation.
	 */
	public Integer size() {
	  return entry.getKey().size();
	}
	

	@Override
  public String toString() {
	    return "Valuation [entry=" + entry + "]";
  }
	
	
}