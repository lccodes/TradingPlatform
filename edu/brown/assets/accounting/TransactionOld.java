package brown.assets.accounting;

import brown.assets.value.Tradeable;
import brown.securities.SecurityOld;

public class TransactionOld implements Tradeable {
	private final SecurityOld security;
	private final double count;
	private final Integer ID;
	private final double price;
	private final long timestamp;
	private final boolean PENDING;
	
	/**
	 * Empty constructor for kryo; do not use
	 */
	public TransactionOld() {
		this.security = null;
		this.count = 0;
		this.ID = null;
		this.price = 0;
		this.timestamp = 0;
		this.PENDING = true;
	}
	
	/**
	 * Constructor for transaction
	 * @param security : which security this transaction pertains to
	 * @param count : number of shares
	 * @param ID : ID of the agent
	 * @param price : price that it was transacted at
	 * @param pending : completed or pending?
	 */
	public TransactionOld(SecurityOld security, double count, Integer ID, double price, boolean pending) {
		this.security = security;
		this.count = count;
		this.ID = ID;
		this.price = price;
		this.PENDING = pending;
		this.timestamp = System.currentTimeMillis();
	}
	
	/**
	 * Gets the security
	 * @return security
	 */
	public SecurityOld getSecurity() {
		return security;
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
		return ID;
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
		return this.PENDING;
	}

	@Override
	public void setAgentID(Integer ID) {
		//Noop
	}
}
