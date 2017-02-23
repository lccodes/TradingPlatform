package brown.test.auction;

import java.util.Map.Entry;

import brown.assets.accounting.Account;
import brown.assets.value.SecurityType;
import brown.assets.value.State;
import brown.assets.value.Tradeable;

public class TheGood implements Tradeable {
	private Integer agentID;
	
	public TheGood() {
		this.agentID = null;
	}
	
	public TheGood(Integer agentID) {
		this.agentID = agentID;
	}

	@Override
	public Integer getAgentID() {
		return this.agentID;
	}

	@Override
	public double getCount() {
		return 1;
	}

	@Override
	public void setAgentID(Integer ID) {
		this.agentID = ID;
	}

	@Override
	public void setCount(double count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Entry<SecurityType, Integer> getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account close(State closingState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tradeable split(double newCount) {
		// TODO Auto-generated method stub
		return null;
	}

}
