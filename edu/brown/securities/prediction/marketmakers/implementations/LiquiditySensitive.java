package brown.securities.prediction.marketmakers.implementations;

import brown.securities.prediction.structures.PMBackend;

public class LiquiditySensitive extends PMBackend {
	
	/*
	 * Alpha can be thought of as the commission of the MM
	 * Theory via Pennock et al:
	 * https://www.cs.cmu.edu/~sandholm/liquidity-sensitive%20automated%20market%20maker.teac.pdf
	 * @alpha : commission of the market maker
	 */
	public LiquiditySensitive(double alpha) {
		this.yes = 1;
		this.no = 1;
		this.b = 0;
		this.alpha = alpha;
	}
	
	/*
	 * Price function
	 * @param direction : boolean
	 * @return price : double
	 */
	public double price(boolean direction) {
		double eYes = Math.exp(yes/getB());
		double eNo = Math.exp(no/getB());
		double first = alpha*Math.log(eYes + eNo);
		double a = no * (eYes - eNo);
		double b = yes * (eNo - eYes);
		double denom = (no+yes)*(eNo + eYes);
		double p1 = first + a/denom;
		double p2 = first + b/denom;
		
		return (direction ? p1 : p2)/(p1+p2);
	}
	
	/*
	 * Price function
	 * @param direction : boolean
	 * @return price : double
	 */
	private double testPrice(double yes, double no, boolean direction) {
		double eYes = Math.exp(yes/(alpha*(yes+no)));
		double eNo = Math.exp(no/(alpha*(yes+no)));
		double first = alpha*Math.log(eYes + eNo);
		double a = no * (eYes - eNo);
		double b = yes * (eNo - eYes);
		double denom = (no+yes)*(eNo + eYes);
		double p1 = first + a/denom;
		double p2 = first + b/denom;
		
		return (direction ? p1 : p2)/(p1+p2);
	}
	
	/*
	 * Cost function
	 * @param qd1 : new quantity yes
	 * @param qd2 : new quantity no
	 * @return cost : double
	 */
	public double cost(double newq1, double newq2) {
		return getB()*Math.log(Math.exp((newq1 + yes)/getB()) + Math.exp((newq2+no)/getB()))
				- getB()*Math.log(Math.exp(yes/getB()) + Math.exp(no/getB()));
	}
	
	/*
	 * Computes b(q)
	 */
	protected double getB() {
		return alpha * (yes + no);
	}
	
	/*
	 * Returns a share to an agent that buys yes
	 * @param shareNum : int
	 */
	@Override
	public void yes(Integer agentID, double shareNum) {
		this.yes += shareNum;
	}
	
	/*
	 * Returns a share to an agent that buys no
	 * @param shareNum : int
	 */
	@Override
	public void no(Integer agentID, double shareNum) {
		this.no += shareNum;
	}
	
	/*
	 * Removes the ability to override b
	 * @throws UnsupportedOperation
	 */
	@Override
	public void setB(double b) {
		throw new UnsupportedOperationException();
	}
	
	/*
	 * How many shares does it take to get to the desired price
	 * @param desired price : double 
	 */
	public double howMany(double price, boolean direction) {
		double amt = .00001;
		while(Math.abs(testPrice(direction ? yes+amt : yes,direction ? no : no+amt,true) - price) > .00001) {
			amt += .00001;
			if (amt > 5) {
				return 1000;
			}
		}
		return amt;
	}
	
	/*
	 * How many shares does it take to fill this budget
	 */
	public double budgetToShares(double budget, boolean direction) {
		double top = !direction ? this.yes : this.no;
		double side = direction ? this.yes : this.no;
		return getB()
		    * Math.log(Math.exp(budget
		        / getB()
		        + Math.log(Math.pow(Math.E, this.yes / getB())
		            + Math.pow(Math.E, this.no / getB())))
		        - Math.exp(top / getB())) - side;
	}
	
	public static void main(String[] args) {
		LiquiditySensitive luke = new LiquiditySensitive(.2);
		System.out.println(luke.ask(1));
		/*luke.no(null, 100);
		luke.yes(null, 100);
		*/
		luke.no(null, 50);
		luke.yes(null, 50);
		System.out.println(luke.ask(1));
		luke.no(null, 50);
		luke.yes(null, 50);
		System.out.println(luke.ask(1));
		luke.no(null, 100);
		luke.yes(null, 100);
		System.out.println(luke.ask(1));
	}

}
