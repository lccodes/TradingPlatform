package brown.auctions.twosided;

import brown.assets.value.FullType;
import brown.auctions.IMarketWrapper;

public interface ITwoSidedWrapper extends IMarketWrapper {	
	/**
	 * Gets the full type of tradeable
	 * @return FullType
	 */
	public FullType getTradeableType();
}
