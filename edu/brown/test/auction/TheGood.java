package brown.test.auction;

import brown.assets.value.Good;

public class TheGood implements Good {
	private Integer agentID;
	
	public TheGood() {
		this.agentID = null;
	}
	
	public TheGood(Integer agentID) {
		this.agentID = agentID;
	}

	@Override
	public Integer getAgentID() {
		return this.agentID;
	}

	@Override
	public double getCount() {
		return 1;
	}

	@Override
	public void setAgentID(Integer ID) {
		this.agentID = ID;
	}

}
