package brown.markets;

import brown.agent.Agent;
import brown.assets.accounting.Ledger;
import brown.assets.value.FullType;
import brown.messages.markets.MarketOrder;

public class LMSR implements ITwoSidedAuction, ITwoSidedPriceTaker {
	private final LMSRServer LMSR;
	private final Ledger LEDGER;
	
	public LMSR() {
		this.LMSR = null;
		this.LEDGER = null;
	}
	
	public LMSR(LMSRServer lmsr, Ledger ledger) {
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
	
	/**
	 * Gets the price
	 * @return price : double
	 */
	public double price() {
		return this.LMSR.price();
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
	
	/**
	 * Gets the number of shares that will cost X monies
	 * @param monies
	 * @return shareNum
	 */
	public double moniesToShares(double monies) {
		return this.LMSR.moniesToShares(monies);
	}
	
	/**
	 * Gets the number of shares that it will take to 
	 * move the price to price
	 * @param price
	 * @return
	 */
	public double priceToShares(double price) {
		return this.LMSR.priceToShares(price);
	}
}
