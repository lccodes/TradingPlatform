package brown.markets;

import brown.assets.value.FullType;

public interface ITwoSidedAuction extends IMarket {	
	/**
	 * Gets the full type of tradeable
	 * @return FullType
	 */
	public FullType getTradeableType();
}
