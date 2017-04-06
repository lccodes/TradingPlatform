package brown.auctions.crules;

import java.util.List;

import brown.assets.accounting.Account;
import brown.assets.value.FullType;
import brown.assets.value.ITradeable;
import brown.assets.value.StateOfTheWorld;

public class ShortShare implements ITradeable {
	private final double COUNT;
	private final FullType TYPE;
	
	public ShortShare(double count, FullType type) {
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

}
