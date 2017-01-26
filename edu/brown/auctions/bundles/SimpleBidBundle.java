package brown.auctions.bundles;

import brown.auctions.BidBundle;

public class SimpleBidBundle implements BidBundle {
	private final double Bid;
	private final Integer AgentID;
	
	/**
	 * Empty constructor for kryo net
	 */
	public SimpleBidBundle() {
		this.Bid = 0;
		this.AgentID = null;
	}
	
	/**
	 * Use this constructor
	 * SimpleBidBundle is a singular double bid
	 * @param bid : agent's bid
	 * @param agent : agent ID
	 */
	public SimpleBidBundle(double bid, Integer agent) {
		this.Bid = bid;
		this.AgentID = agent;
	}

	@Override
	public double getCost() {
		return this.Bid;
	}

	@Override
	public Integer getAgent() {
		return this.AgentID;
	}
}
