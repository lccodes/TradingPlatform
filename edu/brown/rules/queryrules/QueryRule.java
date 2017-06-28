package brown.rules.queryrules;

import brown.assets.accounting.Ledger;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.markets.TradeRequest;
import brown.rules.paymentrules.PaymentType;

public interface QueryRule {

	public TradeRequest wrap(Ledger ledger, PaymentType type, MarketInternalState sTATE);

}
