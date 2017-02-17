package brown.auctions.rules;

import java.util.Map;
import java.util.Set;

import brown.assets.value.Tradeable;
import brown.auctions.bundles.BidBundle;
import brown.messages.auctions.Bid;

public interface PaymentRule {

	Map<BidBundle, Set<Tradeable>> getPayments(Map<Integer, Set<Tradeable>> allocations, Set<Bid> bids);

}
