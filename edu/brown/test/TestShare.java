package brown.test;

import brown.assets.value.Good;

public class TestShare implements Good {
	private final Integer ID;
	private final Integer COUNT;
	
	public TestShare() {
		this.ID = null;
		this.COUNT = null;
	}
	
	public TestShare(Integer ID, int count) {
		this.ID = ID;
		this.COUNT = count;
	}

	@Override
	public Integer getAgentID() {
		return ID;
	}

	@Override
	public int getCount() {
		return COUNT.intValue();
	}

}
