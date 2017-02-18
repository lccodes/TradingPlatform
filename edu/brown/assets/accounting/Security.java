package brown.assets.accounting;

import brown.assets.value.Tradeable;
import brown.auctions.TwoSidedAuction;

public class Security implements Tradeable {
	private final TwoSidedAuction TSA;
	private final double count;
	private Integer agentID;
	private final double price;
	private final long timestamp;
	private final boolean CONVERTABLE;
	
	/**
	 * Empty constructor for kryo; do not use
	 */
	public Security() {
		this.TSA = null;
		this.count = 0;
		this.agentID = null;
		this.price = 0;
		this.timestamp = 0;
		this.CONVERTABLE = true;
	}
	
	/**
	 * Constructor for transaction
	 * @param TSA : which TSA this transaction pertains to
	 * @param count : number of shares
	 * @param ID : ID of the agent
	 * @param price : price that it was transacted at
	 * @param pending : completed or pending?
	 */
	public Security(TwoSidedAuction TSA, double count, Integer ID, double price, boolean convertable) {
		this.TSA = TSA;
		this.count = count;
		this.agentID = ID;
		this.price = price;
		this.CONVERTABLE = convertable;
		this.timestamp = System.currentTimeMillis();
	}
	
	/**
	 * Gets the TSA
	 * @return TSA
	 */
	public TwoSidedAuction getAuction() {
		return TSA;
	}
	
	/**
	 * Gets the share count
	 * @return double : count
	 */
	public double getCount() {
		return count;
	}
	
	/**
	 * Gets the agent that owns the transaction
	 * @return id
	 */
	public Integer getAgentID() {
		return agentID;
	}
	
	/**
	 * Gets the price transacted at
	 * @return price
	 */
	public double getTransactedPrice() {
		return price;
	}
	
	/**
	 * Gets the time transacted at
	 * @return time
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Gets pending status
	 */
	public boolean isPending() {
		return this.CONVERTABLE;
	}

	@Override
	public void setAgentID(Integer ID) {
		this.agentID = ID;
	}
}
