package brown.tradeables;

import java.util.List;

import brown.assets.accounting.Account;
import brown.assets.value.BasicType;
import brown.assets.value.TradeableType;
import brown.states.StateOfTheWorld;

/**
 * Implementation of a good for Lab3.
 * 
 * @author lcamery
 */
public class Lab8Good extends Tradeable {

	/**
	 * Id of the agent that currently owns the good.
	 */
	private Integer agentId;
	private final Integer goodID;
	private final BasicType TYPE;

	/**
	 * Empty Constructor.
	 */
	public Lab8Good() {
		this.goodID = null;
		this.TYPE = null;
	}

	/**
	 * Constructor.
	 * 
	 * @param agentId
	 * the id of the agent who owns the good.
	 */
	public Lab8Good(Integer goodID) {
		this.agentId = null;
		this.goodID = goodID;
		this.TYPE = new BasicType(TradeableType.Good, this.goodID);
	}
	

	@Override
	public Integer getAgentID() {
		return this.agentId;
	}

	@Override
	public void setAgentID(Integer ID) {
		this.agentId = ID;
	}

	@Override
	public double getCount() {
		return 1;
	}

	@Override
	public void setCount(double count) {
		// Noop
	}

	@Override
	public BasicType getType() {
		return this.TYPE;
	}

	@Override
	public List<Account> convert(StateOfTheWorld closingState) {
		return null;
	}

	@Override
	public Tradeable split(double newCount) {
		return null;
	}

	@Override
	public Tradeable toAgent(Integer ID) {
		return this;
	}
	
	@Override
	public String toString() {
		return "Lab3Good-"+this.goodID;
	}

}
