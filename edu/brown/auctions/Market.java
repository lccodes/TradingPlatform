package brown.auctions;

import brown.auctions.arules.AllocationType;

public interface Market {
	/**
	 * Gets the ID of the auction
	 * @return id
	 */
	public Integer getID();
	
	/**
	 * Is the market closed
	 * @return true if ended
	 */
	public boolean isClosed();
	
	/**
	 * What type of allocation mechanism does it use?
	 * @return AllocationType
	 */
	public AllocationType getMechanismType();

}
