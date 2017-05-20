package brown.auctions.interfaces;

import brown.messages.auctions.Bid;

public interface ActivityRule {

	public boolean isAcceptable(MarketInternalState state, Bid bid);

}
