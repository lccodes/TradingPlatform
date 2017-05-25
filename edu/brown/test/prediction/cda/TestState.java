package brown.test.prediction.cda;

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
