package brown.bundles;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import brown.assets.value.BasicType;


public class SimpleBidBundle implements BidBundle {
	private final Map<BasicType,MarketState> BIDS;
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
	public SimpleBidBundle(Map<BasicType, MarketState> bids) {
		if (bids == null) {
			throw new IllegalArgumentException("Null bids");
		}
		this.BIDS = bids;
		this.BT = BundleType.Simple;
	}

	@Override
	public double getCost() {
		double total = 0;
		for (MarketState b : this.BIDS.values()) {
			total += b.PRICE;
		}
		return total;
	}

	@Override
	public BundleType getType() {
		return this.BT;
	}

	@Override
	public BidBundle wipeAgent(Integer ID) {
		Map<BasicType, MarketState> newBids = new HashMap<BasicType, MarketState>();
		for (Entry<BasicType, MarketState> entry : this.BIDS.entrySet()) {
			if (ID.equals(entry.getValue().AGENTID)) {
				newBids.put(entry.getKey(), entry.getValue());
			} else {
				newBids.put(entry.getKey(), new MarketState(null,entry.getValue().PRICE));
			}
		}
		
		return new SimpleBidBundle(newBids);
	}
	
	public MarketState getBid(BasicType type) {
		for (BasicType ot : this.BIDS.keySet()) {
			if(ot.equals(type)) {
				return BIDS.get(type);
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "[" + this.BT + ": " + this.BIDS + "]";
	}

	public boolean isDemanded(BasicType type) {
		return this.getBid(type) != null;
	}
	
	public Set<BasicType> getDemandSet() {
		return this.BIDS.keySet();
	}

	public Set<BasicType> getTradeables() {
		return this.BIDS.keySet();
	}
}
