package brown.test.prediction.cda;

import brown.messages.Registration;

public class PMRegistration extends Registration {
	public final boolean COIN;
	
	public PMRegistration() {
		super(null);
		this.COIN = false;
	}
	
	public PMRegistration(Integer ID, boolean coin) {
		super(ID);
		this.COIN = coin;
	}

}
