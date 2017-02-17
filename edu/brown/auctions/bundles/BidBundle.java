package brown.auctions.bundles;

import java.util.Comparator;

public interface BidBundle {
	
	/**
	 * Gets the aggregate cost of the bid
	 * for verification purposes
	 * @return total cost
	 */
	double getCost();
	
	/**
	 * Which agent owns the bid bundle
	 * @return agent id
	 */
	Integer getAgent();
	
	/**
	 * Removes agent ID
	 * @return BidBundle w/o agent ID
	 */
	BidBundle wipeAgent(Integer ID);

	/**
	 * Identifies the type of the bundle
	 * @return bundleType
	 */
	public BundleType getType();
	
	public static class BidBundleComparator implements Comparator<BidBundle> {

		@Override
		public int compare(BidBundle arg0, BidBundle arg1) {
			return new Double(arg0.getCost()).compareTo(new Double(arg1.getCost()));
		}
		
	}
}
