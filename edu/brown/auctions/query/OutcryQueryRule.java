package brown.auctions.query;

import brown.assets.accounting.Ledger;
import brown.auctions.arules.MechanismType;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.ComplexBidBundle;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.interfaces.MarketInternalState;
import brown.auctions.interfaces.QueryRule;
import brown.auctions.prules.PaymentType;
import brown.auctions.wrappers.ComplexAuction;
import brown.auctions.wrappers.SimpleAuction;
import brown.messages.markets.TradeRequest;

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
