package brown.auctions;

import brown.auctions.arules.MechanismType;

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
	public MechanismType getMechanismType();

}
