package brown.valuation; 

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import brown.assets.value.BasicType;

/**
 * Valuation bundle class. A group of Valuations. Is iterable
 * and has properties of both a hashmap and a set.
 * @author acoggins
 *
 */
public class ValuationBundle implements Iterable<Valuation> {
	
	private Map<Set<BasicType>, Double> valMap; 
	
	/**
	 * constructs an empty valuation bundle.
	 */
	public ValuationBundle() {
		this.valMap = new HashMap<Set<BasicType>, Double>();
	}
	
	public ValuationBundle(ValuationBundle b) {
	  this.valMap = new HashMap<Set<BasicType>, Double>();
	  this.addAll(b);
	}
	
	public ValuationBundle(Map<Set<BasicType>, Double> aMap) {
	  this.valMap = new HashMap<Set<BasicType>, Double>(aMap);
	  }
	
	/**
	 * adds a valuation to the bundle. 
	 * @param val
	 * a valuation.
	 */
	public void add(Valuation val) {
		valMap.put(val.getGoods(), val.getPrice());
	}
	
	/**
	 * adds a valuation to the bundle. 
	 * @param goods
	 * a set of fulltype, goods.
	 * @param price
	 * the price of the goods.
	 */
	public void add(Set<BasicType> goods, Double price) {
		valMap.put(goods, price);
	}
	
	
	/**
	 * empties the bundle.
	 */
	public void clear() {
		valMap = new HashMap<Set<BasicType>, Double>();
	}
	
	/**
	 * checks for the existence of a set of fulltype.
	 * @param goods
	 * a set of fulltype.
	 * @return
	 * true if the set is contained, false otherwise.
	 */
	public Boolean contains(Set<BasicType> goods) {
	  return valMap.containsKey(goods);
	}
	
	
	public Boolean contains(Valuation aValuation) {
		return valMap.containsKey(aValuation.getGoods());
	}
	
	/**
	 * gets a valuation, given a set of goods. Can be used to find a set of goods'
	 * price. 
	 * @param  private Map<FullType, Double> singleValMap; goods
	 * a set of fulltype.
	 * @return
	 * the valuation associated with that type.
	 */
	public Valuation getValuation(Set<BasicType> goods) {
		return new Valuation(goods, valMap.get(goods));
	}
	
	public Double getOrDefault(Set<BasicType> goods, Double defVal) {
	  if(valMap.containsKey(goods)) {
	    return valMap.get(goods);
	  }
	  else {
	    return defVal;
	  }
	}

	
	/**
	 * 
	 * @return
	 * is the bundle empty or not.
	 */
	public Boolean isEmpty() {
		return (valMap.isEmpty());
	}
	
	/**
	 * adds a map from sets of fulltype to double to the valuation bundle.
	 * @param vals
	 * a map from sets of fulltype to double.
	 */
	public void addAll(Map<Set<BasicType>, Double> vals) {
	  valMap.putAll(vals);
	}
	
	
	/**
	 * combines valuation bundle with input bundle. 
	 * @param vals
	 * another valuation bundle.
	 */
	public void addAll(ValuationBundle vals) {
	  for(Valuation v : vals) {
	    this.add(v);
	  }
	}
	
	/**
	 * removes a specified valuation. 
	 * @param val
	 * a valuation
	 */
	public void remove(Valuation val) {
	  valMap.remove(val.getGoods());
	}
	
	/**
	 * returns the size of the bundle.
	 * @return
	 * the size of the bundle.
	 */
	public Integer size() {
	  return valMap.size();
	}
	
	/**
	 * converts the bundle to an array. 
	 * @return
	 * an array of valuations.
	 */
	public Valuation[] toArray() {
	  Valuation[] valArray = new Valuation[this.size()];
	  int i = 0; 
	  for(Set<BasicType> goods : valMap.keySet()) {
	    valArray[i] = this.getValuation(goods);
	    i++;
	  }
	  return valArray;
	}
	
	/**
	 * iterator for valuationbundle
	 * read-only, apparently. May need to fix this along the way somewhere. 
	 */
  public Iterator<Valuation> iterator() {
    ValuationIterator v = new ValuationIterator(this);
    return v;
}

  @Override
  public String toString() {
    return "ValuationBundle " + valMap;
  }



}