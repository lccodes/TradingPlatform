package brown.securities.mechanisms.cda;

import java.util.SortedMap;

import brown.agent.Agent;
import brown.assets.accounting.Transaction;
import brown.securities.SecurityType;
import brown.securities.SecurityWrapper;

public class CDAWrapper implements SecurityWrapper {
	private final Integer ID;
	private final SortedMap<Double,Transaction> toBuy;
	private final SortedMap<Double, Transaction> toSell;
	
	public CDAWrapper(Integer ID, SortedMap<Double, Transaction> toBuy, SortedMap<Double, Transaction> toSell) {
		this.ID = ID;
		this.toBuy = toBuy;
		this.toSell = toSell;
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
	public void buy(Agent agent, double shareNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sell(Agent agent, double shareNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double bid(double shareNum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double ask(double shareNum) {
		// TODO Auto-generated method stub
		return 0;
	}

}
