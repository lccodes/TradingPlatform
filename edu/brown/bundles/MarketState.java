package brown.bundles;

public class MarketState {
	public final Integer AGENTID;
	public final double PRICE;
	
	/**
	 * For kryo do not use
	 */
	public MarketState() {
		this.AGENTID = null;
		this.PRICE = 0;
	}
	
	public MarketState(Integer agentID, double price) {
		this.AGENTID = agentID;
		this.PRICE = price;
	}
	
	@Override
	public String toString() {
		return "<" + this.AGENTID + "," + this.PRICE + ">";
	}
}
