package brown.valuation;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;


import org.apache.commons.math3.distribution.UniformRealDistribution;

import brown.assets.value.BasicType;

/**
 * gives a valuation distributed over a uniform distribution. 
 * TODO: implement this.
 * @author acoggins
 *
 */
public class UniformValuation implements IValuation{
	private Set<BasicType> goods; 
	private Function<Integer, Double> valfunction; 
	private Boolean monotonic; 
	private UniformRealDistribution dist;
	private Double valueScale;

	/**
	 * Constructor for UniformValuation.
	 * @param goods
	 * The goods to be given values. 
	 * @param valFunction
	 * the function that determines the relationship between
	 * the size of a bundle and its value.
	 * @param isMonotonic
	 * Is every bundle of goods strictly preferred to its subsets? 
	 * @param valueScale
	 * one good is default valued at one. This serves as a coefficient for this
	 * default value.
	 * TODO: more args?
	 */
	public UniformValuation (Set<BasicType> goods, Function<Integer, Double> valFunction, 
			 Boolean isMonotonic, Double valueScale) {
		this.goods = goods; 
		this.valfunction = valFunction; 
		this.monotonic = isMonotonic; 
		this.dist = new UniformRealDistribution(); //maybe add args for distributoin
		this.valueScale = valueScale;
	}
	
	@Override
	public ValuationBundle getAllValuations() {
		return null;
	}
	
	@Override
	public ValuationBundle getSomeValuations(Integer numberOfvaluations, 
			Integer bundleSizeMean, Double bundleSizeStdDev) {
		return null; 
	}


}

