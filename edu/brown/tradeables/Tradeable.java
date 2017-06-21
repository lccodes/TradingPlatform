package brown.tradeables;

import java.util.List;
import java.util.function.Function;

import brown.assets.accounting.Account;
import brown.assets.value.EndState;
import brown.assets.value.FullType;
import brown.states.StateOfTheWorld;

/**
 * All non-cash assets will extend this class
 * Primarily for implementing stock or prediction markets
 */
public class Tradeable {
	protected double count;
	protected Integer agentID;
	protected Function<EndState, List<Account>> CONVERTER;
	
	protected final long TIMESTAMP;
	protected final FullType TYPE;
	
	/**
	 * For KyroNet
	 */
	public Tradeable() {
		this.count = 0;
		this.agentID = null;
		this.CONVERTER = null;
		this.TIMESTAMP = 0;
		this.TYPE = null;
	}
	
	/**
	 * Simple Good
	 * @param type
	 * @param count
	 * @param owner
	 */
	public Tradeable(FullType type, double count, Integer owner) {
		this.count = count;
		this.TYPE = type;
		this.agentID = owner;
		
		this.TIMESTAMP = System.currentTimeMillis();
		this.CONVERTER = (state) -> null;
	}
	
	/**
	 * Simple good w/o owner
	 * @param type
	 * @param count
	 */
	public Tradeable(FullType type, double count) {
		this.count = count;
		this.TYPE = type;
		this.agentID = null;
		
		this.TIMESTAMP = System.currentTimeMillis();
		this.CONVERTER = (state) -> null;
	}
	
	/**
	 * Contract
	 * @param type
	 * @param count
	 * @param owner
	 * @param closure
	 */
	public Tradeable(FullType type, double count, Integer owner,
			Function<EndState, List<Account>> closure) {
		this.count = count;
		this.TYPE = type;
		this.agentID = owner;
		
		this.TIMESTAMP = System.currentTimeMillis();
		this.CONVERTER = closure;
	}
	
	/**
	 * Contract w/o owner
	 * @param type
	 * @param count
	 * @param closure
	 */
	public Tradeable(FullType type, double count,
			Function<EndState, List<Account>> closure) {
		this.count = count;
		this.TYPE = type;
		this.agentID = null;
		
		this.TIMESTAMP = System.currentTimeMillis();
		this.CONVERTER = closure;
	}
	
	public Integer getAgentID() {
		return this.agentID;
	}
	
	public void setAgentID(Integer ID) {
		this.agentID = ID;
	}
	
	public double getCount() {
		return this.count;
	}

	public void setCount(double count) {
		this.count = count;
	}
	
	public FullType getType() {
		return this.TYPE;
	}
	
	public List<Account> convert(StateOfTheWorld closingState) {
		return this.CONVERTER.apply(new EndState(this.count, closingState));
	}
	
	public Tradeable split(double newCount) {
		this.count -= newCount;
		return new Tradeable(this.TYPE, newCount, this.agentID, this.CONVERTER);
	}
	
	public Tradeable toAgent(Integer id) {
		Integer toInclude = null;
		if (id.equals(this.agentID)) {
			toInclude = this.agentID;
		}
		
		return new Tradeable(this.TYPE, this.count, toInclude);
	}
}
