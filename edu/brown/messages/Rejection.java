package brown.messages;

import brown.messages.auctions.Bid;
import brown.messages.markets.LimitOrder;
import brown.messages.markets.PurchaseRequest;
import brown.messages.trades.NegotiateRequest;

public class Rejection extends Message {
	public final PurchaseRequest failedPR;
	public final LimitOrder failedLO;
	public final Bid failedBR;
	public final NegotiateRequest failedTR;
	
	/**
	 * Empty for kryo
	 * DO NOT USE
	 */
	public Rejection() {
		super(null);
		this.failedBR = null;
		this.failedPR = null;
		this.failedTR = null;
		this.failedLO = null;
	}

	/**
	 * Rejection for a purchase request;
	 * notifies the agent that they sent an improper request
	 * @param ID : rejection ID
	 * @param pr : rejected purchase request
	 */
	public Rejection(Integer ID, PurchaseRequest pr) {
		super(ID);
		this.failedBR = null;
		this.failedPR = pr;
		this.failedTR = null;
		this.failedLO = null;
	}
	
	/**
	 * Rejection for a bid;
	 * notifies the agent that they sent an improper request
	 * @param ID : rejection ID
	 * @param bid : rejected bid
	 */
	public Rejection(Integer ID, Bid bid) {
		super(ID);
		this.failedBR = bid;
		this.failedPR = null;
		this.failedTR = null;
		this.failedLO = null;
	}
	
	/**
	 * Rejection for a trade request;
	 * notifies the agent that they sent an improper request
	 * @param ID : rejection ID
	 * @param tr : rejected trade request
	 */
	public Rejection(Integer ID, NegotiateRequest tr) {
		super(ID);
		this.failedBR = null;
		this.failedPR = null;
		this.failedTR = tr;
		this.failedLO = null;
	}
	
	/**
	 * For rejected limit orders
	 * @param ID : rejection ID
	 * @param lo : rejected limit order
	 */
	public Rejection(Integer ID, LimitOrder lo) {
		super(ID);
		this.failedLO = lo;
		this.failedBR = null;
		this.failedPR = null;
		this.failedTR = null;
	}

}
