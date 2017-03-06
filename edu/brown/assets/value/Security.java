package brown.assets.value;

import java.util.function.Function;

import brown.assets.accounting.Account;

public class Security implements ITradeable {
	protected double count;
	protected Integer agentID;
	
	protected final long TIMESTAMP;
	protected final FullType TYPE;
	
	
	/**
	 * Empty constructor for kryo; do not use
	 */
	public Security() {
		this.count = 0;
		this.agentID = null;
		this.TIMESTAMP = 0;
		this.TYPE = null;
	}
	
	/**
	 * Security constructor.
	 * @param agentID : owner
	 * @param count : amount
	 * @param type : type
	 * @param closure : what to do when it is closed; nullable
	 */
	public Security(Integer agentID, double count, FullType type) {
		this.agentID = agentID;
		this.count = count;
		this.TIMESTAMP = System.currentTimeMillis();
		this.TYPE = type;
	}
	
	/**
	 * Gets the share count
	 * @return double : count
	 */
	public double getCount() {
		return count;
	}
	
	/**
	 * Gets the agent that owns the transaction
	 * @return id
	 */
	public Integer getAgentID() {
		return agentID;
	}
	
	/**
	 * Gets the time transacted at
	 * @return time
	 */
	public long getTimestamp() {
		return this.TIMESTAMP;
	}

	@Override
	public void setAgentID(Integer ID) {
		this.agentID = ID;
	}

	@Override
	public void setCount(double count) {
		this.count = count;
	}

	@Override
	public Account close(StateOfTheWorld closingState) {
		return null;
	}
	
	public void setClosure(Function<StateOfTheWorld, Account> close) {
		//Noop
	}

	@Override
	public ITradeable split(double newCount) {
		this.count = this.count - newCount;
		return new Security(this.agentID, newCount, this.TYPE);
	}

	@Override
	public FullType getType() {
		return this.TYPE;
	}

	@Override
	public ITradeable toAgent() {
		return new AgentSecurity(this);
	}
}
