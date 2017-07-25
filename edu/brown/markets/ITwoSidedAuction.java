package brown.markets;

import brown.assets.value.BasicType;

public interface ITwoSidedAuction extends IMarket {	
	/**
	 * Gets the full type of tradeable
	 * @return FullType
	 */
	public BasicType getTradeableType();
}
