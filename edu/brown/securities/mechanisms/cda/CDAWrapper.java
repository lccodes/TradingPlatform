package brown.securities.mechanisms.cda;

import java.util.SortedMap;

import brown.agent.Agent;
import brown.assets.accounting.SecurityTwo;
import brown.auctions.TwoSidedWrapper;
import brown.messages.markets.PurchaseRequest;
import brown.securities.SecurityType;

public class CDAWrapper implements TwoSidedWrapper {
	private final ContinuousDoubleAuction CDA;
	
	public CDAWrapper() {
		this.CDA = null;
	}
	
	public CDAWrapper(ContinuousDoubleAuction CDA) {
		this.CDA = CDA;
	}

	@Override
	public Integer getID() {
		return CDA.getID();
	}

	@Override
	public SecurityType getType() {
		return CDA.getType();
	}

	@Override
	public void buy(Agent agent, double shareNum, double sharePrice) {
		agent.CLIENT.sendTCP(new PurchaseRequest(0,this.CDA, shareNum, sharePrice));
	}

	@Override
	public void sell(Agent agent, double shareNum, double sharePrice) {
		agent.CLIENT.sendTCP(new PurchaseRequest(this.CDA, shareNum, sharePrice, 0));
	}

	@Override
	public double bid(double shareNum, double sharePrice) {
		return this.CDA.bid(shareNum, sharePrice);
	}

	@Override
	public double ask(double shareNum, double sharePrice) {
		return this.CDA.ask(shareNum, sharePrice);
	}

	@Override
	public SortedMap<Double, SecurityTwo> getBuyBook() {
		return this.CDA.getBuyBook();
	}

	@Override
	public SortedMap<Double, SecurityTwo> getSellBook() {
		return this.CDA.getSellBook();
	}

}
