package brown.messages;

import brown.agent.Agent;
import brown.messages.auctions.Bid;
import brown.messages.markets.MarketOrder;
import brown.messages.trades.NegotiateRequest;

public class Ack extends Message {
	public final MarketOrder failedLO;
	public final Bid failedBR;
	public final NegotiateRequest failedTR;
	public final boolean REJECTED;
	
	/**
	 * Empty for kryo
	 * DO NOT USE
	 */
	public Ack() {
		super(null);
		this.failedBR = null;
		this.failedTR = null;
		this.failedLO = null;
		this.REJECTED = true;
	}
	
	/**
	 * Rejection for a bid;
	 * notifies the agent that they sent an improper request
	 * @param ID : rejection ID
	 * @param bid : rejected bid
	 */
	public Ack(Integer ID, Bid bid, boolean rejected) {
		super(ID);
		this.failedBR = bid;
		this.failedTR = null;
		this.failedLO = null;
		this.REJECTED = rejected;
	}
	
	/**
	 * Rejection for a trade request;
	 * notifies the agent that they sent an improper request
	 * @param ID : rejection ID
	 * @param tr : rejected trade request
	 */
	public Ack(Integer ID, NegotiateRequest tr, boolean rejected) {
		super(ID);
		this.failedBR = null;
		this.failedTR = tr;
		this.failedLO = null;
		this.REJECTED = rejected;
	}
	
	/**
	 * For rejected limit orders
	 * @param ID : rejection ID
	 * @param lo : rejected limit order
	 */
	public Ack(Integer ID, MarketOrder lo, boolean rejected) {
		super(ID);
		this.failedLO = lo;
		this.failedBR = null;
		this.failedTR = null;
		this.REJECTED = rejected;
	}
	
	@Override
	public void dispatch(Agent agent) {
		agent.onAck(this);
	}

}
