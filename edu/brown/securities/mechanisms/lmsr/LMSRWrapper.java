package brown.securities.mechanisms.lmsr;

import java.util.Set;
import java.util.SortedMap;

import brown.agent.Agent;
import brown.assets.accounting.Transaction;
import brown.assets.value.Tradeable;
import brown.auctions.TwoSidedAuction;
import brown.auctions.TwoSidedWrapper;
import brown.messages.markets.LimitOrder;
import brown.securities.SecurityType;

public class LMSRWrapper implements TwoSidedWrapper {
	private final TwoSidedAuction LMSR;
	
	public LMSRWrapper() {
		this.LMSR = null;
	}
	
	public LMSRWrapper(LMSRYes yes) {
		this.LMSR = yes;
	}
	
	public LMSRWrapper(LMSRNo no) {
		this.LMSR = no;
	}

	@Override
	public Integer getID() {
		return this.LMSR.getID();
	}

	@Override
	public SecurityType getType() {
		return this.LMSR.getType();
	}

	@Override
	public void buy(Agent agent, double shareNum, double sharePrice) {
		agent.CLIENT.sendTCP(new LimitOrder(0, this.LMSR, shareNum,0,sharePrice));
	}

	@Override
	public void sell(Agent agent, double shareNum, double sharePrice) {
		agent.CLIENT.sendTCP(new LimitOrder(0, this.LMSR, 0,shareNum,sharePrice));
	}

	@Override
	public double bid(double shareNum, double sharePrice) {
		return this.LMSR.quoteBid(shareNum, sharePrice);
	}

	@Override
	public double ask(double shareNum, double sharePrice) {
		return this.LMSR.quoteAsk(shareNum, sharePrice);
	}

	@Override
	public SortedMap<Double, Set<Transaction>> getBuyBook() {
		return null;
	}

	@Override
	public SortedMap<Double, Set<Tradeable>> getSellBook() {
		return null;
	}

}
