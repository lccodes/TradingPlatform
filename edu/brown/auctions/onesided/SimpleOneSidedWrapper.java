package brown.auctions.onesided;

import brown.agent.Agent;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.prules.PaymentType;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidReqeust;

public class SimpleOneSidedWrapper extends IOneSidedWrapper {
	
	public SimpleOneSidedWrapper() {
		super(null, null);
	}
	
	public SimpleOneSidedWrapper(PaymentType type, BidReqeust br) {
		super(type, br);
	}

	/**
	 * Bids in this auction
	 * @param agent
	 * @param bid
	 */
	public void bid(Agent agent, double bid) {
		agent.CLIENT.sendTCP(new Bid(0, new SimpleBidBundle(bid, agent.ID, BundleType.Simple), 
				this.BR.AuctionID, agent.ID));
	}
	
	/**
	 * Gets the current high bid in the auction
	 * @return double bid
	 */
	public double getQuote() {
		return this.BR.Current.getCost();
	}
	
	/**
	 * Gets the current high bidder
	 * @return integer : agent ID
	 */
	public Integer getAgent() {
		return this.BR.Current.getAgent();
	}

	@Override
	public void dispatchMessage(Agent agent) {
		agent.onSimpleSealed(this);
	}
}
