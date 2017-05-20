package brown.assets.value;

import java.util.List;

import brown.assets.accounting.Account;

public class UnitGood implements ITradeable {
	private Integer agentID;
	private final String GOODID;
	private double quantity;
	
	/**
	 * New good with unit quantity
	 * @param ID : string ID
	 */
	public UnitGood(String ID) {
		this.GOODID = ID;
		this.quantity = 1;
		this.agentID = null;
	}
	
	/**
	 * New good with custom quantity
	 * @param ID : string ID
	 * @param quantity : quantity
	 */
	public UnitGood(String ID, double quantity) {
		this.GOODID = ID;
		this.quantity = quantity;
		this.agentID = null;
	}

	@Override
	public Integer getAgentID() {
		return this.agentID;
	}

	@Override
	public void setAgentID(Integer ID) {
		this.agentID = ID;
	}

	@Override
	public double getCount() {
		return this.quantity;
	}

	@Override
	public void setCount(double count) {
		this.quantity = count;
	}

	@Override
	public FullType getType() {
		return null;
	}

	@Override
	public List<Account> convert(StateOfTheWorld closingState) {
		return null;
	}

	@Override
	public ITradeable split(double newCount) {
		this.quantity -= newCount;
		return new UnitGood(this.GOODID, newCount);
	}

	@Override
	public ITradeable toAgent(Integer id) {
		return new UnitGood(this.GOODID, this.quantity);
	}

}
