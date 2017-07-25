package brown.tradeables;

import java.util.List;

import brown.assets.accounting.Account;
import brown.assets.value.BasicType;
import brown.states.StateOfTheWorld;

/**
 * Luke wrote this, so I am not exactly sure what it is for.
 * Possibly testing.
 * @author lcamery
 *
 */
public class TheGood extends Tradeable {
	private Integer agentID;
	
	/**
	 * for Kryo
	 */
	public TheGood() {
		this.agentID = null;
	}
	
	/**
	 * Constructor just contains an agent id and nothing else.
	 * @param agentID
	 */
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
	public Tradeable split(double newCount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BasicType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tradeable toAgent(Integer ID) {
		return this;
	}

}
