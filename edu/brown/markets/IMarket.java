package brown.markets;

import brown.agent.Agent;
import brown.assets.accounting.Ledger;

public interface IMarket {
	/**
	 * Gets the ID of the auction
	 * @return id
	 */
	public Integer getAuctionID();
	
	/**
	 * Gets the ledger for this market
	 * @return ledger
	 */
	public Ledger getLedger();
	
	/**
	 * Handles the double dispatch for 
	 * trade request types
	 * @param agent
	 */
	public void dispatchMessage(Agent agent);
}
