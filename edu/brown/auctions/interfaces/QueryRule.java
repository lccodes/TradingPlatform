package brown.auctions.interfaces;

import brown.assets.accounting.Ledger;
import brown.auctions.prules.PaymentType;
import brown.messages.markets.TradeRequest;

public interface QueryRule {

	public TradeRequest wrap(Ledger ledger, PaymentType type, MarketInternalState sTATE);

}
