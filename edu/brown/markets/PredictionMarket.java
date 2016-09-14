package brown.markets;

/**
 * Public accesors to private PM
 * @author lcamery
 *
 */
public class PredictionMarket {
	protected final PM pm;
	
	public PredictionMarket(PM internal) {
		this.pm = internal;
	}
	
	/*
	 * Quotes the cumulative price for a certain number of yes shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double getPriceYes(int shareNum) {
		return pm.getPriceYes(shareNum);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of no shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double getPriceNo(int shareNum) {
		return pm.getPriceNo(shareNum);
	}
	
	/*
	 * Returns a share to an agent that buys yes
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 * @return share : share object; extendable in real games
	 */
	public Share buyYes(Integer agentID, int shareNum) {
		return pm.buyYes(agentID, shareNum);
	}
	
	/*
	 * Returns a share to an agent that buys no
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 * @return share : share object; extendable in real games
	 */
	public Share buyNo(Integer agentID, int shareNum) {
		return pm.buyNo(agentID, shareNum);
	}

}
