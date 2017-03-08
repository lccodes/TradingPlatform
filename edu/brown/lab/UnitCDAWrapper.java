package brown.lab;

import java.util.SortedMap;

import brown.agent.Agent;
import brown.assets.value.ITradeable;
import brown.securities.mechanisms.cda.CDAWrapper;

public class UnitCDAWrapper implements ITwoSidedUnitSetter {
	private final CDAWrapper WRAP;
	
	public UnitCDAWrapper(CDAWrapper wrapper) {
		this.WRAP = wrapper;
	}

	@Override
	public void buy(Agent agent, int sharePrice) {
		this.WRAP.buy(agent, 1, sharePrice);
	}

	@Override
	public void sell(Agent agent, int sharePrice) {
		this.WRAP.sell(agent, 1, sharePrice);
	}

	@Override
	public void cancel(Agent agent, boolean buy, int sharePrice) {
		this.WRAP.cancel(agent, buy, 1, sharePrice);
	}

	@Override
	public int quoteBid(int sharePrice) {
		return (int) this.WRAP.quoteBid(1, sharePrice);
	}

	@Override
	public int quoteAsk(int sharePrice) {
		return (int) this.WRAP.quoteAsk(1, sharePrice);
	}

	@Override
	public SortedMap<Double, Double> getBuyBook() {
		return this.WRAP.getBuyBook();
	}

	@Override
	public SortedMap<Double, Double> getSellBook() {
		return this.WRAP.getSellBook();
	}

	@Override
	public ITradeable getTradeable() {
		return this.WRAP.getTradeable();
	}

	@Override
	public Integer getAuctionID() {
		return this.WRAP.getAuctionID();
	}

	@Override
	public void dispatchMessage(Agent agent) {
		//Noop
	}
	
	

}
