package brown.assets.value;

public class EndState {
	public final double QUANTITY;
	public final StateOfTheWorld STATE;
	
	public EndState(double quantity, StateOfTheWorld state) {
		this.QUANTITY = quantity;
		this.STATE = state;
	}
}