package brown.valuation;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;


import org.apache.commons.math3.distribution.UniformRealDistribution;

import brown.assets.value.FullType;

public class UniformValuation implements IValuation{
	private Set<FullType> goods; 
	private Function<Integer, Double> valfunction; 
	private Boolean monotonic; 
	private UniformRealDistribution dist;
	private Double valueScale;

	
	public UniformValuation (Set<FullType> goods, Function<Integer, Double> valFunction, 
			 Boolean isMonotonic, Double valueScale) {
		this.goods = goods; 
		this.valfunction = valFunction; 
		this.monotonic = isMonotonic; 
		this.dist = new UniformRealDistribution(); //maybe add args for distributoin
		this.valueScale = valueScale;
	}
	
	@Override
	public Map<Set<FullType>, Double> getAllValuations() {
		return null;
	}
	
	@Override
	public Map<Set<FullType>, Double> getSomeValuations(Integer numberOfvaluations, 
			Integer bundleSizeMean, Double bundleSizeStdDev) {
		return null; 
	}


}

