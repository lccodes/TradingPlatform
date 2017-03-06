package brown.assets.value;

import java.util.function.Function;

import brown.assets.accounting.Account;

public class Contract extends Security {
	private Function<StateOfTheWorld, Account> CONVERTER;
	
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
			Function<StateOfTheWorld,Account> closure) {
		super(agentID, count, type);
		if (closure == null) {
			this.CONVERTER = state -> null;
		} else {
			this.CONVERTER = closure;
		}
	}
	
	@Override
	public Account convert(StateOfTheWorld closingState) {
		return this.CONVERTER.apply(closingState);
	}
	
	/**
	 * Set the closure function
	 */
	public void setClosure(Function<StateOfTheWorld, Account> close) {
		this.CONVERTER = close;
	}
	
	@Override
	public ITradeable split(double newCount) {
		this.count = this.count - newCount;
		return new Contract(this.agentID, newCount, this.TYPE, this.CONVERTER);
	}

}
