package brown.valuation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.math3.distribution.NormalDistribution;
import brown.assets.value.FullType;

public class NormalValuation implements Valuation{
	private Set<FullType> GOODS; 
	private Function<Integer, Double> VALFUNCTION; 
	private Double EXPECTEDCOVARIANCE;
	private Boolean MONOTONIC; 
	private NormalDistribution DIST;
	private Double VALUESCALE;

	
	public NormalValuation (Set<FullType> goods, Function<Integer, Double> valFunction, 
			Double expectedCovariance, Boolean isMonotonic, Double valueScale) {
		this.GOODS = goods; 
		this.VALFUNCTION = valFunction; 
		this.EXPECTEDCOVARIANCE = expectedCovariance; 
		this.MONOTONIC = isMonotonic; 
		this.DIST = new NormalDistribution(); //maybe add args for distributoin
		this.VALUESCALE = valueScale;
		
		
		
	}
	
	@Override
	public Map<Set<FullType>, Double> getTotalValuation() {
		return null;
	}
	
	@Override
	public Map<Set<FullType>, Double> getValuation(Integer numberOfvaluations, 
			Integer bundleSizeMean, Double bundleSizeStdDev, Double valueScale) {
		return null; 
	}


}

