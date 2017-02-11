package brown.auctions.bundles;

import brown.auctions.BidBundle;
import brown.auctions.BundleType;

public class SimpleBidBundle implements BidBundle {
	private final double Bid;
	private final Integer AgentID;
	private final BundleType BT;
	
	/**
	 * Empty constructor for kryo net
	 */
	public SimpleBidBundle() {
		this.Bid = 0;
		this.AgentID = null;
		this.BT = null;
	}
	
	/**
	 * Use this constructor
	 * SimpleBidBundle is a singular double bid
	 * @param bid : agent's bid
	 * @param agent : agent ID
	 */
	public SimpleBidBundle(double bid, Integer agent, BundleType type) {
		this.Bid = bid;
		this.AgentID = agent;
		this.BT = type;
	}

	@Override
	public double getCost() {
		return this.Bid;
	}

	@Override
	public Integer getAgent() {
		return this.AgentID;
	}

	@Override
	public BundleType getType() {
		return this.BT;
	}

	@Override
	public BidBundle wipeAgent() {
		return new SimpleBidBundle(this.Bid, null, this.BT);
	}
}
