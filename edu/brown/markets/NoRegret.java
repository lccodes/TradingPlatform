package brown.markets;

import java.util.HashMap;
import java.util.Map;

public abstract class NoRegret extends LMSRBackend {
	protected Map<Integer, Double> weights;
	
	public NoRegret(Integer ID, double b, NoRegretMemory memory) {
		super(ID, b);
		this.weights = memory.WEIGHTS;
		if (this.weights == null) {
			this.weights = new HashMap<Integer, Double>();
		}
	}
	
	public abstract NoRegretMemory update(boolean outcome);
	
	public static class NoRegretMemory {
		public final Map<Integer, Double> WEIGHTS;
		public final double T;
		
		public NoRegretMemory(Map<Integer, Double> weights, double T) {
			this.WEIGHTS = weights;
			this.T = T;
		}
	}
}
