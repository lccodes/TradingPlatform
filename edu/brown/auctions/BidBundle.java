package brown.auctions;

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
}
