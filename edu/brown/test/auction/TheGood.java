package brown.test.auction;

import java.util.List;

import brown.assets.accounting.Account;
import brown.assets.value.FullType;
import brown.assets.value.ITradeable;
import brown.assets.value.StateOfTheWorld;

public class TheGood implements ITradeable {
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
		//Noop
	}

	@Override
	public List<Account> convert(StateOfTheWorld closingState) {
		// Noop
		return null;
	}

	@Override
	public ITradeable split(double newCount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FullType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITradeable toAgent(Integer ID) {
		return this;
	}

}
