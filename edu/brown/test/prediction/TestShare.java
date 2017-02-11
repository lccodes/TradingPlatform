package brown.test.prediction;

import brown.assets.value.Tradeable;

public class TestShare implements Tradeable {
	private final Integer ID;
	private final double COUNT;
	
	public TestShare() {
		this.ID = null;
		this.COUNT = 0;
	}
	
	public TestShare(Integer ID, double count) {
		this.ID = ID;
		this.COUNT = count;
	}

	@Override
	public Integer getAgentID() {
		return ID;
	}

	@Override
	public double getCount() {
		return COUNT;
	}

	@Override
	public void setAgentID(Integer ID) {
		//Noop
	}

}
