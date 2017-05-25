package brown.auctions.interfaces;

import java.util.Map;
import java.util.Set;

import brown.assets.value.Tradeable;

public interface IBidBundle {
	/**
	 * Gives the market a complex bid bundle
	 * Set<ITradeable> -> Double
	 * 
	 * @return complex bundle
	 */
	public Map<Set<Tradeable>, Double> getComplexBundle();
	
	/**
	 * Gives the market a simple bid bundle
	 * ITradeable -> Double
	 * @return simple bid bundle
	 */
	public Map<Tradeable, Double> getSimpleBidBundle();
	
	/**
	 * Gives the market a complex bid bundle
	 * from a demand query
	 * @param tradeables
	 * @return complex bid bundle
	 */
	public Map<Set<Tradeable>, Double> getComplexDemandQuery(Set<Tradeable> tradeables);
}
