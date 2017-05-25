package brown.auctions;

import brown.agent.Agent;
import brown.assets.accounting.Ledger;

public interface IMarketWrapper {
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
