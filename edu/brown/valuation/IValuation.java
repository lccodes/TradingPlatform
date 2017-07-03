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
	 * @param bundleSizeMean
	 * @param bundleSizeStdDev
	 * @return
	 */
	public Map<Set<FullType>, Double> getSomeValuations (Integer numberOfValuations, 
			Integer bundleSizeMean, Double bundleSizeStdDev);
	
}