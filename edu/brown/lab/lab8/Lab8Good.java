package brown.lab.lab8;

import java.util.List;

import brown.assets.accounting.Account;
import brown.assets.value.FullType;
import brown.assets.value.ITradeable;
import brown.assets.value.StateOfTheWorld;
import brown.assets.value.TradeableType;

/**
 * Implementation of a good for Lab3.
 * 
 * @author lcamery
 */
public class Lab8Good implements ITradeable {

	/**
	 * Id of the agent that currently owns the good.
	 */
	private Integer agentId;
	private final Integer goodID;
	private final FullType TYPE;

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
	 */
	public Lab8Good(Integer goodID) {
		this.agentId = null;
		this.goodID = goodID;
		this.TYPE = new FullType(TradeableType.Custom, this.goodID);
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
	public FullType getType() {
		return this.TYPE;
	}

	@Override
	public List<Account> convert(StateOfTheWorld closingState) {
		return null;
	}

	@Override
	public ITradeable split(double newCount) {
		return null;
	}

	@Override
	public ITradeable toAgent(Integer ID) {
		return this;
	}
	
	@Override
	public String toString() {
		return "Lab3Good-"+this.goodID;
	}

}