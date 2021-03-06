package brown.markets;


public abstract class OnlineLearning extends LMSRBackend {
	protected double pastWeight;
	
	public OnlineLearning(Integer ID, double b, double yes, double no) {
		super(ID, b,yes,no);
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
