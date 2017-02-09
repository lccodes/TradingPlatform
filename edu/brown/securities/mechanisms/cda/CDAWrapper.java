package brown.securities.mechanisms.cda;

import java.util.SortedMap;

import brown.agent.Agent;
import brown.assets.accounting.Transaction;
import brown.messages.markets.LimitOrder;
import brown.securities.SecurityType;
import brown.securities.SecurityWrapper;

public class CDAWrapper implements SecurityWrapper {
	private final Integer ID;
	private final ContinuousDoubleAuction CDA;
	private final SortedMap<Double,Transaction> toBuy;
	private final SortedMap<Double, Transaction> toSell;
	
	public CDAWrapper(Integer ID, ContinuousDoubleAuction cda,
			SortedMap<Double, Transaction> toBuy, SortedMap<Double, Transaction> toSell) {
		this.ID = ID;
		this.toBuy = toBuy;
		this.toSell = toSell;
		this.CDA = cda;
		//TODO: Strip info from these
	}

	@Override
	public Integer getID() {
		return this.ID;
	}

	@Override
	public SecurityType getType() {
		return SecurityType.CDA;
	}

	@Override
	public void buy(Agent agent, double shareNum, double sharePrice) {
		agent.CLIENT.sendTCP(new LimitOrder(0, this.CDA, shareNum, 0, sharePrice));
	}

	@Override
	public void sell(Agent agent, double shareNum, double sharePrice) {
		agent.CLIENT.sendTCP(new LimitOrder(0, this.CDA, 0, -1 *shareNum, sharePrice));
	}

	@Override
	public double bid(double shareNum) {
		return this.CDA.bid(shareNum);
	}

	@Override
	public double ask(double shareNum) {
		return this.CDA.ask(shareNum);
	}

	@Override
	public void buy(Agent agent, double shareNum) {
		agent.CLIENT.sendTCP(new LimitOrder(0, this.CDA, shareNum, 0, Double.MAX_VALUE));
	}

	@Override
	public void sell(Agent agent, double shareNum) {
		agent.CLIENT.sendTCP(new LimitOrder(0, this.CDA, 0, -1 *shareNum, Double.MIN_VALUE));
	}

	@Override
	public double bid(double shareNum, double sharePrice) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double ask(double shareNum, double sharePrice) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SortedMap<Double, Transaction> getBuyOrders() {
		return this.toBuy;
	}

	@Override
	public SortedMap<Double, Transaction> getSellOrders() {
		return this.toSell;
	}

}
