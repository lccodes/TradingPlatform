package brown.test.prediction.lmsr;

import brown.assets.value.StateOfTheWorld;

public class TestState implements StateOfTheWorld {
	private int STATE;
	
	public TestState(boolean heads) {
		this.STATE = heads ? 1 : 0;
	}

	@Override
	public int getState() {
		return this.STATE;
	}

}
