package brown.auctions.query;

import brown.assets.accounting.Ledger;
import brown.auctions.arules.MechanismType;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.ComplexBidBundle;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.interfaces.MarketInternalState;
import brown.auctions.interfaces.QueryRule;
import brown.auctions.prules.PaymentType;
import brown.auctions.wrappers.ComplexWrapper;
import brown.auctions.wrappers.SimpleWrapper;
import brown.messages.markets.TradeRequest;

public class SealedBidQuery implements QueryRule {

	@Override
	public TradeRequest wrap(Ledger ledger, PaymentType type, MarketInternalState state) {
		if (state.getAllocation().getType().equals(BundleType.Simple)) {
			return new TradeRequest(0, 
					new SimpleWrapper(state.getID(), ledger, type, MechanismType.SealedBid, 
							(SimpleBidBundle) state.getReserve()), 
							MechanismType.OpenOutcry);
		} else {
			return new TradeRequest(0, 
					new ComplexWrapper(state.getID(), ledger, type, MechanismType.SealedBid, 
							(ComplexBidBundle) state.getReserve()), 
							MechanismType.OpenOutcry);
		}
	}

}