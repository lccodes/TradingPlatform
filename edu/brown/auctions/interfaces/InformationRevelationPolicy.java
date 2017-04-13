package brown.auctions.interfaces;

public interface InformationRevelationPolicy {

	public MarketInternalState handleInfo(Integer ID, MarketInternalState state);

}
