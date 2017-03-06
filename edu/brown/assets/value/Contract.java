package brown.assets.value;

import java.util.function.Function;

import brown.assets.accounting.Account;

public class Contract extends Security {
	private Function<StateOfTheWorld, Account> CLOSURE;
	
	public Contract() {
		super(null,0,null);
		this.CLOSURE = null;
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
			this.CLOSURE = state -> null;
		} else {
			this.CLOSURE = closure;
		}
	}
	
	@Override
	public Account close(StateOfTheWorld closingState) {
		return this.CLOSURE.apply(closingState);
	}
	
	/**
	 * Set the closure function
	 */
	public void setClosure(Function<StateOfTheWorld, Account> close) {
		this.CLOSURE = close;
	}
	
	@Override
	public ITradeable split(double newCount) {
		this.count = this.count - newCount;
		return new Contract(this.agentID, newCount, this.TYPE, this.CLOSURE);
	}

}
