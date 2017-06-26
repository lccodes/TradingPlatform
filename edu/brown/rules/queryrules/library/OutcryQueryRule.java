package brown.rules.queryrules.library;

import brown.assets.accounting.Ledger;
import brown.auctions.arules.MechanismType;
import brown.bundles.BundleType;
import brown.bundles.ComplexBidBundle;
import brown.bundles.SimpleBidBundle;
import brown.marketinternalstates.MarketInternalState;
import brown.markets.ComplexAuction;
import brown.markets.SimpleAuction;
import brown.messages.markets.TradeRequest;
import brown.paymentrules.PaymentType;
import brown.rules.queryrules.QueryRule;

public class OutcryQueryRule implements QueryRule {

	@Override
	public TradeRequest wrap(Ledger ledger, PaymentType type, MarketInternalState state) {
		if (state.getAllocation().getType().equals(BundleType.Simple)) {
			return new TradeRequest(0, 
					new SimpleAuction(state.getID(), ledger, type, MechanismType.OpenOutcry, 
							(SimpleBidBundle) state.getAllocation(), state.getEligibility()), 
							MechanismType.OpenOutcry);
		} else {
			return new TradeRequest(0, 
					new ComplexAuction(state.getID(), ledger, type, MechanismType.OpenOutcry, 
							(ComplexBidBundle) state.getAllocation()), 
							MechanismType.OpenOutcry);
		}
	}

}
