package brown.securities.lmsr.marketmakers.implementations;

import javax.swing.text.Utilities;

import brown.securities.prediction.marketmakers.types.OnlineLearning;

public class OnlineWeighedAverage extends OnlineLearning {

	public OnlineWeighedAverage(Integer ID, double b, double yes, double no) {
		super(ID, b, yes, no);
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * Returns a share to an agent that buys yes
	 * @param shareNum : int
	 */
	@Override
	public void yes(Integer agentID, double shareNum) {
		throw new UnsupportedOperationException();
		//TODO: We need new methods to take belief
	}
	
	/**
	 * Let's an agent enter the market and returns their position
	 * Positive = X yes shaers; Negative = X no shares.
	 * @param agentID
	 * @param budget
	 * @param value
	 * @return
	 */
	public double enterMarket(Integer agentID, double budget, double value) {
		double weightedAverage = (pastWeight*this.price(true) + budget*value)/(budget+pastWeight);
		double newB = Utilities.findBWeighted(new Double[]{value}, new Double[]{budget}, 
				weightedAverage, yes, no);
		if (newB <= 0) {
			return 0;
		}
		
		this.b = newB;

		boolean dir = value > price(true);
		double idealShareNum = howMany(value, dir);
		double idealCost = dir ? cost(idealShareNum, 0) : cost(0, idealShareNum);
		double shareNum = idealCost > budget ? budgetToShares(budget, dir) : idealShareNum;
		
		pastWeight += shareNum;
		if(dir) {
			yes += shareNum;
			return shareNum;
		} else {
			no += shareNum;
			return -1 * shareNum;
		}	
	}
	
	/*
	 * Returns a share to an agent that buys no
	 * @param shareNum : int
	 */
	@Override
	public void no(Integer agentID, double shareNum) {
		throw new UnsupportedOperationException();
	}

	@Override
	public OnlineMemory getMemory() {
		return new OnlineMemory(pastWeight,yes,no);
	}

}
