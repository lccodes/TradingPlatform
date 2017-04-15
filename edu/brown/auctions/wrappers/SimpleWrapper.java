package brown.auctions.wrappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import brown.agent.Agent;
import brown.assets.accounting.Ledger;
import brown.assets.value.FullType;
import brown.auctions.IMarketWrapper;
import brown.auctions.arules.MechanismType;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.prules.PaymentType;
import brown.messages.auctions.Bid;
import brown.setup.Logging;

public class SimpleWrapper implements IMarketWrapper {
	private final Integer ID;
	private final Ledger LEDGER;
	private final SimpleBidBundle HIGHBID;
	
	private final PaymentType PTYPE;
	private final MechanismType MTYPE;
	
	public SimpleWrapper() {
		this.ID = null;
		this.LEDGER = null;
		this.HIGHBID = null;
		this.PTYPE = null;
		this.MTYPE = null;
	}
	
	/**
	 * Contructor
	 * @param ID
	 * @param ledger
	 * @param highBid
	 */
	public SimpleWrapper(Integer ID, Ledger ledger, PaymentType ptype, MechanismType mtype,
			SimpleBidBundle highBid) {
		if (highBid == null || ledger == null) {
			throw new IllegalArgumentException("Null structures");
		}
		this.ID = ID;
		this.LEDGER = ledger;
		this.HIGHBID = highBid;
		this.PTYPE = ptype;
		this.MTYPE = mtype;
	}

	@Override
	public Ledger getLedger() {
		return this.LEDGER;
	}

	@Override
	public void dispatchMessage(Agent agent) {
		switch(this.MTYPE) {
		case ContinuousDoubleAuction:
			break;
		case LMSR:
			break;
		case Lemonade:
			break;
		case OpenOutcry:
			agent.onSimpleOpenOutcry(this);
			break;
		case SealedBid:
			agent.onSimpleSealed(this);
			break;
		default:
			Logging.log("[X] unknown PaymentType");
			break;
		}
	}

	@Override
	public Integer getAuctionID() {
		return this.ID;
	}
	
	/**
	 * Returns the payment type
	 * @return
	 */
	public PaymentType getPaymentType() {
		return this.PTYPE;
	}
	
	/**
	 * Returns the high bid
	 * @return double
	 */
	public BidBundle.BidderPrice getHighBid(FullType type) {
		return this.HIGHBID.getBid(type);
	}

	public void bid(Agent agent, Map<FullType, Double> bids) {
		Map<FullType, BidBundle.BidderPrice> fixedBids = new HashMap<FullType,BidBundle.BidderPrice>();
		for (Entry<FullType, Double> bid : bids.entrySet()) {
			fixedBids.put(bid.getKey(), new BidBundle.BidderPrice(agent.ID, bid.getValue()));
		}
		agent.CLIENT.sendTCP(new Bid(0,new SimpleBidBundle(fixedBids),this.ID,agent.ID));
	}

}
