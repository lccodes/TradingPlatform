package brown.assets.value;

import brown.assets.accounting.Account;

public class Good implements ITradeable {
	private Integer agentID;
	private double count;
	private final FullType TYPE;
	
	public Good() {
		this.agentID = null;
		this.count = 0;
		this.TYPE = null;
	}
	
	public Good(Integer agentID, double count, Integer goodID) {
		this.agentID = agentID;
		this.count = count;
		this.TYPE = new FullType(TradeableType.Good, goodID);
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
		return this.count;
	}

	@Override
	public void setCount(double count) {
		this.count = count;
	}

	@Override
	public FullType getType() {
		return this.TYPE;
	}

	@Override
	public Account convert(StateOfTheWorld closingState) {
		return null;
	}

	@Override
	public ITradeable split(double newCount) {
		this.count -= newCount;
		return new Good(this.agentID, newCount, this.TYPE.ID);
	}

	@Override
	public ITradeable toAgent() {
		return this;
	}

}
