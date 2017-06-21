package brown.states;


public class TestStateCDA implements StateOfTheWorld {
	private int STATE;
	
	public TestStateCDA(boolean heads) {
		this.STATE = heads ? 1 : 0;
	}

	@Override
	public int getState() {
		return this.STATE;
	}

}
