package brown.assets.accounting;

import java.util.function.Function;

import brown.assets.value.State;
import brown.assets.value.Tradeable;
import brown.securities.SecurityType;

public class Security implements Tradeable {
	private double count;
	private Integer agentID;
	
	private final long TIMESTAMP;
	private final SecurityType TYPE;
	private final Function<State, Account> CLOSURE;
	
	
	/**
	 * Empty constructor for kryo; do not use
	 */
	public Security() {
		this.count = 0;
		this.agentID = null;
		this.TIMESTAMP = 0;
		this.TYPE = null;
		this.CLOSURE = null;
	}
	
	/**
	 * Security constructor.
	 * @param agentID : owner
	 * @param count : amount
	 * @param type : type
	 * @param closure : what to do when it is closed; nullable
	 */
	public Security(Integer agentID, double count, SecurityType type, Function<State,Account> closure) {
		this.agentID = agentID;
		this.count = count;
		this.TIMESTAMP = System.currentTimeMillis();
		this.TYPE = type;
		if (closure == null) {
			this.CLOSURE = state -> null;
		} else {
			this.CLOSURE = closure;
		}
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
	public Account close(State closingState) {
		return this.CLOSURE.apply(closingState);
	}

	@Override
	public Tradeable split(double newCount) {
		this.count = this.count - newCount;
		return new Security(this.agentID, newCount, this.TYPE, this.CLOSURE);
	}

	@Override
	public SecurityType getType() {
		return this.TYPE;
	}
}
