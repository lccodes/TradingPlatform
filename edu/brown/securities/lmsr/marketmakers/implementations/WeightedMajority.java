package brown.securities.lmsr.marketmakers.implementations;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import brown.securities.prediction.marketmakers.types.NoRegret;

public class WeightedMajority extends NoRegret {
	private Map<Integer, Boolean> predictions;
	private final double T;
	private final double LEARNING;
	private final double FAILURE;

	public WeightedMajority(Integer ID, double b, NoRegretMemory memory) {
		super(ID, b, memory);
		this.T = memory.T;
		this.LEARNING = Math.sqrt(Math.log(memory.WEIGHTS.size())/T);
		this.predictions = new HashMap<Integer, Boolean>();
		
		double failure = 0;
		for (double d : this.weights.values()) {
			failure += Math.exp(-1 * LEARNING * d);
		}
		this.FAILURE = failure;
	}
	
	/*
	 * Returns a share to an agent that buys yes
	 * @param shareNum : int
	 */
	@Override
	public void yes(Integer agentID, double shareNum) {
		this.yes += shareNum * getWeight(agentID);
	}
	
	/*
	 * Returns a share to an agent that buys no
	 * @param shareNum : int
	 */
	@Override
	public void no(Integer agentID, double shareNum) {
		this.no += shareNum * getWeight(agentID);
	}

	@Override
	public NoRegretMemory update(boolean outcome) {
		for (Entry<Integer, Boolean> prediction : this.predictions.entrySet()) {
			if (this.weights.containsKey(prediction.getKey())) {
				this.weights.put(prediction.getKey(), this.weights.get(prediction.getKey())
						+ (outcome == prediction.getValue() ? 0.0 : 1.0));
			} else {
				this.weights.put(prediction.getKey(), (outcome == prediction.getValue() ? 0.0 : 1.0));
			}
		}
		
		return new NoRegret.NoRegretMemory(this.weights, this.T+1);
	}
	
	private double getWeight(Integer ID) {
		if (!this.weights.containsKey(ID)) {
			return 1.0/FAILURE;
		}
		return Math.exp(-1 * LEARNING * this.weights.get(ID)) / this.FAILURE;
	}

}
