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
import brown.rules.paymentrules.PaymentType;
import brown.rules.queryrules.QueryRule;

public class SealedBidQuery implements QueryRule {

	@Override
	public TradeRequest wrap(Ledger ledger, PaymentType type, MarketInternalState state) {
		if (state.getAllocation().getType().equals(BundleType.Simple)) {
			return new TradeRequest(0, 
					new SimpleAuction(state.getID(), ledger, type, MechanismType.SealedBid, 
							(SimpleBidBundle) state.getReserve(), state.getEligibility()), 
							MechanismType.OpenOutcry);
		} else {
			return new TradeRequest(0, 
					new ComplexAuction(state.getID(), ledger, type, MechanismType.SealedBid, 
							(ComplexBidBundle) state.getReserve()), 
							MechanismType.OpenOutcry);
		}
	}

}
