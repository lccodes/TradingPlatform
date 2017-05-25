package brown.auctions.twosided;

import brown.assets.value.FullType;
import brown.auctions.IMarket;

public interface ITwoSidedAuction extends IMarket {	
	/**
	 * Gets the full type of tradeable
	 * @return FullType
	 */
	public FullType getTradeableType();
}
