package brown.securities.mechanisms.cda;

import brown.assets.accounting.Transaction;
import brown.securities.Security;
import brown.securities.SecurityType;
import brown.securities.SecurityWrapper;

public class ContinuousDoubleAuction implements Security {
	private final Integer ID;
	private final SecurityType TYPE;
	
	public ContinuousDoubleAuction(Integer ID, SecurityType type) {
		this.ID = ID;
		this.TYPE = type;
	}

	@Override
	public Integer getID() {
		return this.ID;
	}

	@Override
	public SecurityType getType() {
		return this.TYPE;
	}

	@Override
	public Transaction buy(Integer agentID, double shareNum, double sharePrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction sell(Integer agentID, double shareNum, double sharePrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double cost(double newq1, double newq2) {
		// TODO Auto-generated method stub
		return 0;
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

	@Override
	public SecurityWrapper wrap() {
		// TODO Auto-generated method stub
		return null;
	}

}
