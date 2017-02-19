package brown.securities.prediction.marketmakers.types;

import brown.securities.mechanisms.lmsr.PMBackend;

public abstract class OnlineLearning extends PMBackend {
	protected double pastWeight;
	
	public OnlineLearning(double b, double yes, double no) {
		super(b,yes,no);
		this.pastWeight = 0;
	}
	
	public abstract OnlineMemory getMemory();
	
	public static class OnlineMemory {
		public final double YES;
		public final double NO;
		public final double PASTWEIGHT;
		
		public OnlineMemory(double yes, double no, double pastWeight) {
			this.YES = yes;
			this.NO = no;
			this.PASTWEIGHT = pastWeight;
		}
	}
}
