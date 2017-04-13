package brown.auctions.interfaces;

import java.util.List;

import brown.assets.accounting.Order;
import brown.auctions.bundles.BidBundle;
import brown.auctions.prules.PaymentType;

public interface PaymentRule {

	public List<Order> getPayments(MarketInternalState state);

	public PaymentType getPaymentType();

	public BidBundle getReserve();

}
