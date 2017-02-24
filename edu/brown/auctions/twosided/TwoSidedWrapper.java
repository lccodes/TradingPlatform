package brown.auctions.twosided;

import brown.assets.value.FullType;

public interface TwoSidedWrapper {
	/**
	 * Gets the ID of the auction
	 * @return id
	 */
	public Integer getID();
	
	/**
	 * Gets the full type of tradeable
	 * @return FullType
	 */
	public FullType getType();
}
