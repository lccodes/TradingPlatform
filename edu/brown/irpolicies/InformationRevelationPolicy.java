package brown.irpolicies;

import brown.marketinternalstates.MarketInternalState;

public interface InformationRevelationPolicy {

	public MarketInternalState handleInfo(Integer ID, MarketInternalState state);

}
