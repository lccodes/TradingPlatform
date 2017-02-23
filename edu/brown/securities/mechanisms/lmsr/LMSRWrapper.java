package brown.securities.mechanisms.lmsr;

import java.util.SortedMap;

import brown.agent.Agent;
import brown.assets.value.FullType;
import brown.auctions.TwoSidedAuction;
import brown.auctions.TwoSidedWrapper;
import brown.messages.markets.LimitOrder;

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
	public FullType getType() {
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
	public double quoteBid(double shareNum, double sharePrice) {
		return this.LMSR.quoteBid(shareNum, sharePrice);
	}

	@Override
	public double quoteAsk(double shareNum, double sharePrice) {
		return this.LMSR.quoteAsk(shareNum, sharePrice);
	}

	@Override
	public SortedMap<Double, Double> getBuyBook() {
		return null;
	}

	@Override
	public SortedMap<Double, Double> getSellBook() {
		return null;
	}

}
