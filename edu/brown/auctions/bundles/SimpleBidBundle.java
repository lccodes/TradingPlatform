package brown.auctions.bundles;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import brown.assets.value.FullType;


public class SimpleBidBundle implements BidBundle {
	private final Map<FullType, BidBundle.BidderPrice> BIDS;
	private final BundleType BT;
	
	/**
	 * Empty constructor for kryo net
	 */
	public SimpleBidBundle() {
		this.BIDS = null;
		this.BT = null;
	}
	
	/**
	 * Use this constructor
	 * SimpleBidBundle is a singular double bid
	 * @param bid : agent's bid
	 * @param agent : agent ID
	 */
	public SimpleBidBundle(Map<FullType, BidBundle.BidderPrice> bids) {
		if (bids == null) {
			throw new IllegalArgumentException("Null bids");
		}
		this.BIDS = bids;
		this.BT = BundleType.Simple;
	}

	@Override
	public double getCost() {
		double max = 0;
		for (BidBundle.BidderPrice b : this.BIDS.values()) {
			max = Math.max(b.PRICE, max);
		}
		return max;
	}

	@Override
	public BundleType getType() {
		return this.BT;
	}

	@Override
	public BidBundle wipeAgent(Integer ID) {
		Map<FullType, BidBundle.BidderPrice> newBids = new HashMap<FullType, BidBundle.BidderPrice>();
		for (Entry<FullType, BidderPrice> entry : this.BIDS.entrySet()) {
			if (ID.equals(entry.getValue().AGENTID)) {
				newBids.put(entry.getKey(), entry.getValue());
			} else {
				newBids.put(entry.getKey(), new BidBundle.BidderPrice(null,entry.getValue().PRICE));
			}
		}
		
		return new SimpleBidBundle(newBids);
	}
	
	public BidBundle.BidderPrice getBid(FullType type) {
		for (Entry<FullType, BidderPrice> ot : this.BIDS.entrySet()) {
			if(ot.getKey().equals(type)) {
				return ot.getValue();
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "[" + this.BT + ": " + this.BIDS + "]";
	}

	public boolean isDemanded(FullType type) {
		return this.getBid(type) != null;
	}
}
