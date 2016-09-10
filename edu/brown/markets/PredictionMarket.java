package brown.markets;

import java.util.ArrayList;
import java.util.List;

public abstract class PredictionMarket {
	private final List<Share> yes;
	private final List<Share> no;
	private final double b;
	
	public PredictionMarket(double b) {
		this.yes = new ArrayList<Share>();
		this.no = new ArrayList<Share>();
		this.b = b;
	}
	
	/*
	 * Cost function
	 * @param qd1 : new quantity yes
	 * @param qd2 : new quantity no
	 * @return cost : double
	 */
	private double cost(int newq1, int newq2) {
		return b*Math.log(Math.pow(Math.E, (newq1 + yes.size())/b) + Math.pow(Math.E, (newq2+no.size())/b))
				- b*Math.log(Math.pow(Math.E, yes.size()/b) + Math.pow(Math.E, no.size()));
	}
	
	/*
	 * Quotes the cumulative price for a certain number of yes shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double getPriceYes(int shareNum) {
		return cost(shareNum, 0);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of no shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double getPriceNo(int shareNum) {
		return cost(0, shareNum);
	}
	
	public abstract Share buyYes(int shareNum);
	public abstract Share buyNo(int shareNum);
}
