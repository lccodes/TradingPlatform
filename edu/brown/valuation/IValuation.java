package brown.valuation; 


import java.util.Map;
import java.util.Set;

import brown.assets.value.FullType;

/** interface for generating valuations for agents to use in auctions.
 * 
 * @author acoggins
 *
 */
public interface IValuation {
	
	/**
	 * Gets all valuations given a set of goods. 
	 * @return
	 */
	public Map<Set<FullType>, Double> getAllValuations();
	
	/**
	 * gets a specified number of valuations given a set of goods.
	 * @param numberOfValuations
	 * the total number of valuations to be given. 
	 * @param bundleSizeMean
	 * the mean bundle size of valuations
	 * @param bundleSizeStdDev
	 * the standard deviation of bundle size
	 * @return
	 * a specified number of valuation bundles, 
	 */
	public Map<Set<FullType>, Double> getSomeValuations (Integer numberOfValuations, 
			Integer bundleSizeMean, Double bundleSizeStdDev);
	
}