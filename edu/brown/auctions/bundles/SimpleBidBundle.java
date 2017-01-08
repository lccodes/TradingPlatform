package brown.auctions.bundles;

import brown.auctions.BidBundle;

public class SimpleBidBundle implements BidBundle {
	private final double Bid;
	private final Integer AgentID;
	
	public SimpleBidBundle() {
		this.Bid = 0;
		this.AgentID = null;
	}
	
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
