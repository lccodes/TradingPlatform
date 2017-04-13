package brown.auctions.wrappers;

import brown.agent.Agent;
import brown.assets.accounting.Ledger;
import brown.auctions.IMarketWrapper;
import brown.auctions.arules.MechanismType;
import brown.auctions.bundles.ComplexBidBundle;
import brown.auctions.prules.PaymentType;

public class ComplexWrapper implements IMarketWrapper {

	public ComplexWrapper(Integer id, Ledger ledger, PaymentType type, MechanismType openoutcry,
			ComplexBidBundle allocation) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Integer getAuctionID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ledger getLedger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispatchMessage(Agent agent) {
		// TODO Auto-generated method stub

	}

}
