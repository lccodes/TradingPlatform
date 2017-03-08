package brown.securities.mechanisms.lmsr;

import brown.agent.Agent;
import brown.assets.value.ITradeable;
import brown.auctions.twosided.ITwoSidedPriceTaker;
import brown.auctions.twosided.ITwoSidedWrapper;
import brown.auctions.twosided.TwoSidedAuction;
import brown.messages.markets.MarketOrder;

public class LMSRWrapper implements ITwoSidedWrapper, ITwoSidedPriceTaker {
	private final TwoSidedAuction LMSR;
	
	public LMSRWrapper() {
		this.LMSR = null;
	}
	
	public LMSRWrapper(LMSR lmsr) {
		this.LMSR = lmsr;
	}

	@Override
	public Integer getAuctionID() {
		return this.LMSR.getID();
	}

	@Override
	public ITradeable getTradeable() {
		return this.LMSR.getTradeable();
	}

	@Override
	public void buy(Agent agent, double shareNum, double maxPrice) {
		agent.CLIENT.sendTCP(new MarketOrder(0, this.LMSR, shareNum,0.0,maxPrice));
	}

	@Override
	public void sell(Agent agent, double shareNum, double maxPrice) {
		agent.CLIENT.sendTCP(new MarketOrder(0, this.LMSR, 0,shareNum,maxPrice));
	}

	@Override
	public double quoteBid(double shareNum) {
		return this.LMSR.quoteBid(shareNum, -1);
	}

	@Override
	public double quoteAsk(double shareNum) {
		return this.LMSR.quoteAsk(shareNum, -1);
	}

	@Override
	public void dispatchMessage(Agent agent) {
		agent.onLMSR(this);
	}
}
