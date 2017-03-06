package brown.auctions;

import brown.agent.Agent;

public interface IMarketWrapper {
	/**
	 * Gets the ID of the auction
	 * @return id
	 */
	public Integer getAuctionID();
	
	/**
	 * Handles the double dispatch for 
	 * trade request types
	 * @param agent
	 */
	public void dispatchMessage(Agent agent);
}
