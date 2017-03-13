package brown.securities.mechanisms.lmsr;

import brown.agent.Agent;
import brown.assets.accounting.Ledger;
import brown.assets.value.FullType;
import brown.auctions.twosided.ITwoSidedPriceTaker;
import brown.auctions.twosided.ITwoSidedWrapper;
import brown.auctions.twosided.TwoSidedAuction;
import brown.messages.markets.MarketOrder;

public class LMSRWrapper implements ITwoSidedWrapper, ITwoSidedPriceTaker {
	private final TwoSidedAuction LMSR;
	private final Ledger LEDGER;
	
	public LMSRWrapper() {
		this.LMSR = null;
		this.LEDGER = null;
	}
	
	public LMSRWrapper(LMSR lmsr, Ledger ledger) {
		this.LMSR = lmsr;
		this.LEDGER = ledger;
	}

	@Override
	public Integer getAuctionID() {
		return this.LMSR.getID();
	}

	@Override
	public FullType getTradeableType() {
		return this.LMSR.getTradeableType();
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

	@Override
	public void cancel(Agent agent, boolean buy, double shareNum,
			double maxPrice) {
		if (buy) {
			agent.CLIENT.sendTCP(new MarketOrder(0, this.LMSR, 0,shareNum,maxPrice));
		} else {
			agent.CLIENT.sendTCP(new MarketOrder(0, this.LMSR, shareNum,0.0,maxPrice));
		}
	}

	@Override
	public Ledger getLedger() {
		return this.LEDGER;
	}
}
