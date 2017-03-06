package brown.assets.value;

import brown.assets.accounting.Account;

public class AgentSecurity implements ITradeable {
	private Integer AGENTID;
	private FullType TYPE;
	private double COUNT;
	
	public AgentSecurity() {
		this.AGENTID = null;
		this.TYPE = null;
		this.COUNT = -1;
	}
	
	public AgentSecurity(Security security) {
		this.AGENTID = security.getAgentID();
		this.TYPE = security.getType();
		this.COUNT = security.getCount();
	}

	@Override
	public Integer getAgentID() {
		return this.AGENTID;
	}

	@Override
	public void setAgentID(Integer ID) {
		//Noop
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
	public Account convert(StateOfTheWorld closingState) {
		return null;
	}

	@Override
	public ITradeable split(double newCount) {
		return null;
	}

	@Override
	public ITradeable toAgent() {
		return this;
	}
	
	@Override
	public String toString() {
		return "<" + this.TYPE.TYPE + " " + COUNT + ">";
	}

}
