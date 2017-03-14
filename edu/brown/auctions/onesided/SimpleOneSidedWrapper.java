package brown.auctions.onesided;

import brown.agent.Agent;
import brown.assets.accounting.Ledger;
import brown.auctions.arules.MechanismType;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.prules.PaymentType;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;

public class SimpleOneSidedWrapper extends IOneSidedWrapper {
	private final Ledger LEDGER;
	private final MechanismType TYPE;
	
	public SimpleOneSidedWrapper() {
		super(null, null);
		this.LEDGER = null;
		this.TYPE = null;
	}
	
	public SimpleOneSidedWrapper(PaymentType type, BidRequest br, MechanismType mtype, Ledger ledger) {
		super(type, br);
		this.LEDGER = ledger;
		this.TYPE = mtype;
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
		if (this.TYPE.equals(MechanismType.SealedBid)) {
			agent.onSimpleSealed(this);
		} else if (this.TYPE.equals(MechanismType.OpenOutcry)){
			agent.onSimpleOpenOutcry(this);
		} else if (this.TYPE.equals(MechanismType.Lemonade)) {
			agent.onSimpleSealed(this);
		}
	}

	@Override
	public Ledger getLedger() {
		return this.LEDGER;
	}
}
