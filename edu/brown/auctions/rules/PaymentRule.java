package brown.auctions.rules;

import java.util.Map;
import java.util.Set;

import brown.assets.value.ITradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.prules.PaymentType;
import brown.messages.auctions.Bid;

public interface PaymentRule {

	Map<BidBundle, Set<ITradeable>> getPayments(Map<Integer, Set<ITradeable>> allocations, Set<Bid> bids);

	PaymentType getPaymentType();

}
