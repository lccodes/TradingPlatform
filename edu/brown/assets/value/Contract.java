package brown.assets.value;

import java.util.List;
import java.util.function.Function;

import brown.assets.accounting.Account;

/**
 * Closable security
 * @author lcamery
 *
 */
public class Contract extends Security {
	private Function<EndState, List<Account>> CONVERTER;
	
	/**
	 * For kryo
	 */
	public Contract() {
		super(null,0,null);
		this.CONVERTER = null;
	}
	
	/**
	 * Contract constructor.
	 * @param agentID : owner
	 * @param count : amount
	 * @param type : type
	 * @param closure : what to do when it is closed; nullable
	 */
	public Contract(Integer agentID, double count, FullType type,
			Function<EndState, List<Account>> closure) {
		super(agentID, count, type);
		if (closure == null) {
			this.CONVERTER = state -> null;
		} else {
			this.CONVERTER = closure;
		}
	}
	
	@Override
	public List<Account> convert(StateOfTheWorld closingState) {
		return this.CONVERTER.apply(new EndState(this.count, closingState));
	}
	
	@Override
	public ITradeable split(double newCount) {
		this.count = this.count - newCount;
		return new Contract(this.agentID, newCount, this.TYPE, this.CONVERTER);
	}
	
	public static class EndState {
		public final double QUANTITY;
		public final StateOfTheWorld STATE;
		
		public EndState(double quantity, StateOfTheWorld state) {
			this.QUANTITY = quantity;
			this.STATE = state;
		}
	}

}
