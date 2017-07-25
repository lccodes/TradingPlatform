package brown.bundles;

import java.util.Map;
import java.util.Set;

import brown.assets.value.BasicType;

/**
 * A Complex Bid Bundle is a bid bundle that submits bids for vectors 
 * of goods. i.e. (a b c) = 1
 * @author acoggins
 *
 */
public class ComplexBidBundle implements BidBundle {
	private final Map<Set<BasicType>, MarketState> BIDS;
	private final BundleType BT;
	
	/**
	 * Empty constructor for kryo net
	 */
	public ComplexBidBundle() {
		this.BIDS = null;
		this.BT = null;
	}
	
	/**
	 * Use this constructor
	 * ComplexBidBundle uses Set<FullType>
	 * @param bid : agent's bid
	 * @param agent : agent ID
	 */
	public ComplexBidBundle(Map<Set<BasicType>, MarketState> bid, Integer agent) {
		this.BIDS = bid;
		this.BT = BundleType.Complex;
	}

	@Override
	public double getCost() {
		double max = 0;
		for (MarketState b : this.BIDS.values()) {
			max = Math.max(b.PRICE, max);
		}
		return max;
	}

	@Override
	public BidBundle wipeAgent(Integer ID) {
		return new ComplexBidBundle(this.BIDS, ID);
	}

	@Override
	public BundleType getType() {
		return this.BT;
	}
	
	public MarketState getBid(Set<BasicType> types) {
		return this.BIDS.get(types);
	}

}
