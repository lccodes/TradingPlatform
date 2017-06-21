package brown.markets;

import brown.agent.Agent;
import brown.assets.accounting.Ledger;
import brown.auctions.arules.MechanismType;
import brown.bundles.ComplexBidBundle;
import brown.paymentrules.PaymentType;

public class ComplexAuction implements IMarket {

	public ComplexAuction(Integer id, Ledger ledger, PaymentType type, MechanismType openoutcry,
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
