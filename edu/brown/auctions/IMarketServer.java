package brown.auctions;

import brown.assets.accounting.Ledger;
import brown.auctions.arules.MechanismType;

public interface IMarketServer {
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
	
	/**
	 * Is shortselling permitted?
	 * @return true if shortable
	 */
	public boolean permitShort();

	/**
	 * Get wrapper
	 * @return
	 */
	public IMarket wrap(Ledger ledger);

}
