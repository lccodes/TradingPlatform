package brown.valuation;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import brown.assets.value.FullType;

public class PointValuation implements Valuation {
	private Set<FullType> GOODS; 
	private Function<Integer, Double> VALFUNCTION; 
	private Double VALUESCALE;

	
	public PointValuation (Set<FullType> goods, Function<Integer, Double> valFunction, 
			 Double valueScale) {
		this.GOODS = goods; 
		this.VALFUNCTION = valFunction; 
		this.VALUESCALE = valueScale;	
	}
	
	@Override
	public Map<Set<FullType>, Double> getTotalValuation() {
		
		Map<Set<FullType>, Double> existingSets = new HashMap<Set<FullType>, Double>();
		existingSets.put(new HashSet<FullType>(), 0.0);
		for(int i = 0; i < GOODS.size(); i++) {
			for(FullType good : GOODS) {
				Map<Set<FullType>, Double> temp = new HashMap<Set<FullType>, Double>();
				for(Set<FullType> e : existingSets.keySet()) {
					if (!e.contains(good)) {
						Set<FullType> eCopy = e; 
						eCopy.add(good);
						temp.put(eCopy, VALFUNCTION.apply(eCopy.size()) * VALUESCALE);
					}
				}
				existingSets.putAll(temp);
			}
		}
		return existingSets;
	}
	
	@Override
	public Map<Set<FullType>, Double> getValuation(Integer numberOfvaluations, 
			Integer bundleSizeMean, Double bundleSizeStdDev, Double ValueScale) {
		//get n observations of a normal distribution with specified mean and standard deviation, 
		//round down to the nearest integer. These are our subset sizes. for each one, use a random number
		//to determine the goods to get.
		//subset.size random numbers between 1 and goods.size
		//since no variation, just deal justice.
		
		
		
		return null; 
	}


}
