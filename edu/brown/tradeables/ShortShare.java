package brown.tradeables;

import java.util.List;

import brown.assets.accounting.Account;
import brown.assets.value.BasicType;
import brown.states.StateOfTheWorld;

/**
 * a share intended to be used for shortselling. 
 * @author acoggins
 *
 */
public class ShortShare extends Tradeable {
	private final double COUNT;
	private final BasicType TYPE;
	
	/**
	 * a shortshare has a count and a type.
	 * @param count
	 * the number of goods in the shortshare.
	 * @param type
	 * the Basic type of the good.
	 */
	public ShortShare(double count, BasicType type) {
		this.COUNT = count;
		this.TYPE = type;
	}

	@Override
	public Integer getAgentID() {
		return null;
	}

	@Override
	public void setAgentID(Integer ID) {
		// Noop
	}

	@Override
	public double getCount() {
		return this.COUNT;
	}

	@Override
	public void setCount(double count) {
		//Noop
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

}
