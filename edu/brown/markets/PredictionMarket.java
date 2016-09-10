package brown.markets;

public abstract class PredictionMarket {
	protected final PM pm;
	
	public PredictionMarket(PM internal) {
		this.pm = internal;
	}

}
