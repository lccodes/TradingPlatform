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
	
	public SimpleBidBundle getQuote() {
		return (SimpleBidBundle) this.BR.Current;
	}

	@Override
	public void dispatchMessage(Agent agent) {
		agent.onSimpleSealed(this);
	}
}
