package brown.test.prediction.cda;

import brown.messages.Registration;

public class PMRegistration extends Registration {
	public final boolean COIN;
	public final int DECOYCOUNT;
	
	public PMRegistration() {
		super(null);
		this.COIN = false;
		this.DECOYCOUNT = 0;
	}
	
	public PMRegistration(Integer ID, boolean coin, int decoys) {
		super(ID);
		this.COIN = coin;
		this.DECOYCOUNT = decoys;
	}

}
