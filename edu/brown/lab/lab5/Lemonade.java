package brown.lab.lab5;

import brown.agent.Agent;
import brown.assets.accounting.Ledger;
import brown.auctions.IMarket;
import brown.auctions.onesided.SimpleOneSidedWrapper;

public class Lemonade implements IMarket {
	private final SimpleOneSidedWrapper WRAP;
	
	public Lemonade(SimpleOneSidedWrapper wrapper) {
		this.WRAP = wrapper;
	}

	@Override
	public Integer getAuctionID() {
		return this.WRAP.getAuctionID();
	}

	@Override
	public Ledger getLedger() {
		return this.WRAP.getLedger();
	}

	@Override
	public void dispatchMessage(Agent agent) {
		//Noop
	}
	
	public void pickPosition(Agent agent, int pos) {
		this.WRAP.bid(agent, pos);
	}

}
