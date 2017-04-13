package brown.auctions.interfaces;

import java.util.Map;
import java.util.Set;

import brown.assets.value.ITradeable;

public interface IBidBundle {
	/**
	 * Gives the market a complex bid bundle
	 * Set<ITradeable> -> Double
	 * 
	 * @return complex bundle
	 */
	public Map<Set<ITradeable>, Double> getComplexBundle();
	
	/**
	 * Gives the market a simple bid bundle
	 * ITradeable -> Double
	 * @return simple bid bundle
	 */
	public Map<ITradeable, Double> getSimpleBidBundle();
	
	/**
	 * Gives the market a complex bid bundle
	 * from a demand query
	 * @param tradeables
	 * @return complex bid bundle
	 */
	public Map<Set<ITradeable>, Double> getComplexDemandQuery(Set<ITradeable> tradeables);
}
