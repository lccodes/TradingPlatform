package brown.valuation;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;


import org.apache.commons.math3.distribution.UniformRealDistribution;

import brown.assets.value.FullType;

public class UniformValuation implements Valuation{
	private Set<FullType> GOODS; 
	private Function<Integer, Double> VALFUNCTION; 
	private Boolean MONOTONIC; 
	private UniformRealDistribution DIST;
	private Double VALUESCALE;

	
	public UniformValuation (Set<FullType> goods, Function<Integer, Double> valFunction, 
			 Boolean isMonotonic, Double valueScale) {
		this.GOODS = goods; 
		this.VALFUNCTION = valFunction; 
		this.MONOTONIC = isMonotonic; 
		this.DIST = new UniformRealDistribution(); //maybe add args for distributoin
		this.VALUESCALE = valueScale;
		
		
		
	}
	
	@Override
	public Map<Set<FullType>, Double> getTotalValuation() {
		return null;
	}
	
	@Override
	public Map<Set<FullType>, Double> getValuation(Integer numberOfvaluations, 
			Integer bundleSizeMean, Double bundleSizeStdDev) {
		return null; 
	}


}

