package brown.valuation; 

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import brown.assets.value.FullType;

public class ValuationSet {
	
	private Map<Set<FullType>, Double> valMap; 
	
	public ValuationSet() {
		this.valMap = new HashMap<Set<FullType>, Double>();
	}
	
	public void addValuation(Valuation val) {
		valMap.put(val.getGoods(), val.getPrice());
	}
	
	public void addValuation(Set<FullType> goods, Double price) {
		valMap.put(goods, price);
	}
	
	public void clear() {
		valMap = new HashMap<Set<FullType>, Double>();
	}
	
	public Boolean containsKey(Valuation aValuation) {
		if(valMap.containsKey(aValuation.getGoods())) {
			return true;
		}
		return false; 
	}

	public Set<Valuation> valuationSet() {
		Set<Valuation> valueSet = new HashSet<Valuation>();
		for(Set<FullType> goods : valMap.keySet()) {
			valueSet.add(new Valuation(goods, valMap.get(goods)));
		}
		return valueSet;
	}
	
	public Valuation getValuation(Set<FullType> goods) {
		return new Valuation(goods, valMap.get(goods));
	}
	
	public Boolean isEmpty() {
		if (valMap.isEmpty()) {
			return true; 
		}
		return false;
	}
	
	
	
	
}