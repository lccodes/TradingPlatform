package brown.valuation; 


import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.distribution.NormalDistribution; 
import org.apache.commons.math3.distribution.UniformRealDistribution; 


import brown.assets.value.FullType;

/** interface for making valuations. 
 * 	generalization of SpecValGenerator
 * 
 * @author acoggins
 *
 */
public interface Valuation {
	
	/**
	 * NORMAL, TOTAL. 
	 * Gets a valuation for every possible bundle of input goods. 
	 * Not recommended for larger bundles due to time complexity.
	 * @param goods
	 * 		the set of goods to be valued.
	 * @param dist
	 * 		the distribution of the valuation. In this case, set 
	 * 		to gaussian, s.t. the prices of every good and each 
	 * 		constituative bundle varies according to a normal 
	 * `	distribution.
	 * @param expectedCovariance
	 * 		the average covaraince between goods. A larger
	 * 		average covariance will result in larger bundles
	 * 		having larger variance, and vice versa 
	 * @param isMonotonic
	 * 		Determines whether or not larger bundles have weakly
	 * 		higher value than their subsets. 
	 * @param valueScale
	 * 		scale factor for values. serves as a multiplier for a zero- 
	 * 		to-one default scale.
	 * @return
	 * 		A map of every possible subset of the input goods, with specified 
	 * 		normally distributed values.
	 */
	public Map<Set<FullType>, Double>  getTotalValuation (Set<FullType> goods, 
			NormalDistribution dist, Double expectedCovariance, Boolean isMonotonic,  
			Double valueScale);
	
	/**
	 * UNIFORM, TOTAL. 
	 * Gets a valuation for every possible bundle of input goods. 
	 * Not recommended for larger bundles due to time complexity.
	 * @param goods
	 * 		the set of goods to be valued.
	 * @param dist
	 * 		the distribution of the valuation. In this case, set 
	 * 		to uniform, s.t. the prices of every good and each 
	 * 		constituative bundle varies according to a uniform 
	 * 		distribution.
	 * @param isMonotonic
	 * 		Determines whether or not larger bundles have weakly
	 * 		higher value than their subsets. 
	 * @param valueScale
	 * 		scale factor for values. serves as a multiplier for a zero- 
	 * 		to-one default scale.
	 * @return
	 * 		A map of every possible subset of the input goods, with specified 
	 * 		uniformly distributed values.
	 */
	public Map<Set<FullType>, Double>  getTotalValuation (Set<FullType> goods,
			UniformRealDistribution dist, Boolean isMonotonic, 
			Double valueScale);
	
	/**
	 *  POINT, TOTAL. 
	 *  Gets a point valuation for every possible bundle of input goods. 
	 * Not recommended for larger bundles due to time complexity.
	 * @param goods
	 * 		the set of goods to be valued.
	 * @param isMonotonic
	 * 		Determines whether or not larger bundles have weakly
	 * 		higher value than their subsets. 
	 * @param valueScale
	 * 		scale factor for values. serves as a multiplier for a zero- 
	 * 		to-one default scale.
	 * @return
	 * 		A map of every possible subset of the input goods, with specified 
	 * 		point values.
	 */
	public Map<Set<FullType>, Double>  getTotalValuation (Set<FullType> goods,
		 Boolean isMonotonic, Double valueScale);
	
	/**
	 * NORMAL, PARTIAL.
	 * gets valuations for a specified number of bundles. 
	 * @param goods
	 * 		the set of goods to be valued. 
	 * @param dist
	 * 		the distribution that the valuation of bundles will follow.
	 * 		Set to normal. 
	 * @param expectedCovariance
	 * 		the average covaraince between goods. A larger
	 * 		average covariance will result in larger bundles
	 * 		having larger variance, and vice versa 
	 * @param isMonotonic
	 * 		Determines whether of not larger bundles have weakly higher
	 * 		values than their subsets.
	 * @param NumberOfValuations
	 * 		the number of random bundles to be valued. 
	 * @param bundleSizeMean
	 * 		the mean size of randomly drawn bundles. 
	 * @param bundleSizeStdDev
	 * 		The standard deviation size of randomly drawn bundles.
	 * @param valueScale
	 * 		scale factor for values. Serves as a multiplier for a zero-
	 * 		to-one default scale. 
	 * @return
	 */
	
	public Map<Set<FullType>, Double> getValuation (Set<FullType> goods,
	NormalDistribution dist, Double expectedCovariance, Boolean isMonotonic,
	Integer numberOfValuations, Integer bundleSizeMean, 
	Double bundleSizeStdDev, Double valueScale); 
	
	/**
	 * UNIFORM, PARTIAL. 
	 * gets valuations for a specified number of bundles. 
	 * @param goods
	 * 		the set of goods to be valued. 
	 * @param dist
	 * 		the distribution that the valuation of bundles will follow.
	 * 		Set to uniform. 
	 * @param isMonotonic
	 * 		Determines whether of not larger bundles have weakly higher
	 * 		values than their subsets.
	 * @param NumberOfValuations
	 * 		the number of random bundles to be valued. 
	 * @param bundleSizeMean
	 * 		the mean size of randomly drawn bundles. 
	 * @param bundleSizeStdDev
	 * 		The standard deviation size of randomly drawn bundles.
	 * @param valueScale
	 * 		scale factor for values. Serves as a multiplier for a zero-
	 * 		to-one default scale. 
	 * @return
	 */
	public Map<Set<FullType>, Double> getValuation (Set<FullType> goods,
			UniformRealDistribution dist, Boolean isMonotonic, Integer numberOfValuations,
			Integer bundleSizeMean, Double bundleSizeStdDev, Double valueScale);
	
	/**
	 * POINT, PARTIAL. 
	 * gets valuations for a specified number of bundles.
	 * @param goods
	 * 		The set of goods to be valued.
	 * @param isMonotonic
	 * 		Determines whether or not larger bundles have weakly higher
	 * 		values than their subsets.
	 * @param numberOfValuations
	 * 		the number of random bundles to be valued.
	 * @param bundleSizeMean
	 * 		The mean size of randomly drawn bundles.
	 * @param bundleSizeStdDev
	 * 		The standard deviation of randomly drawn bundles. 
	 * @param valueScale
	 * 		Scale factor for values. Serves as a multiplier for a zero-
	 * 		to-one default scale.
	 * @return
	 */
	public Map<Set<FullType>, Double> getValuation (Set<FullType> goods, 
			Boolean isMonotonic, Integer numberOfValuations, Integer bundleSizeMean, 
			Double bundleSizeStdDev, Double valueScale);






} 