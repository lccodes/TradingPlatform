package brown.messages;

public class Registration extends Message {
	
	/**
	 * Kronet requires an empty constructor
	 */
	public Registration() {
		super(null);
	}

	public Registration(Integer ID) {
		super(ID);
	}
}
