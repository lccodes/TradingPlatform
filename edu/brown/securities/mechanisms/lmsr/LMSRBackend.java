package brown.securities.mechanisms.lmsr;

/**
 * Private backend prediction market implementation
 * Agents are provided pointers to the public face
 * to prevent illegal modifications
 * @author lcamery
 *
 */
public class LMSRBackend {
	private final int ID;
	public double yes;
	public double no;
	public double b;
	public double alpha;
	
	public LMSRBackend() {
		this.yes = 0;
		this.no = 0;
		this.b = 1;
		this.alpha = 0;
		this.ID = -1;
	}
	
	public LMSRBackend(int ID, double b) {
		this.yes = 0;
		this.no = 0;
		this.b = b;
		this.alpha = 0;
		this.ID = ID;
	}
	
	public LMSRBackend(int ID, double b, double yes, double no) {
		this.b = b;
		this.yes = yes;
		this.no = no;
		this.alpha = 0;
		this.ID = ID;
	}
	
	/*
	 * Price function
	 * @param direction : boolean
	 * @return price : double
	 */
	public double price(boolean direction) {
		double up = Math.exp(yes/b);
		double down = Math.exp(no/b);
		return direction ? up/(up+down) : down/(down+up);
	}
	
	/*
	 * Cost function
	 * @param qd1 : new quantity yes
	 * @param qd2 : new quantity no
	 * @return cost : double
	 */
	public double cost(double newq1, double newq2) {
		return b*Math.log(Math.exp((newq1 + yes)/b) + Math.exp((newq2+no)/b))
				- b*Math.log(Math.exp(yes/b) + Math.exp(no/b));
	}
	
	/*
	 * Quotes the cumulative price for a certain number of yes shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double bid(double shareNum) {
		return cost(shareNum, 0);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of no shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double ask(double shareNum) {
		return cost(0, shareNum);
	}
	
	/*
	 * Returns a share to an agent that buys yes
	 * @param shareNum : int
	 */
	public void yes(Integer agentID, double shareNum) {
		this.yes += shareNum;
	}
	
	/*
	 * Returns a share to an agent that buys no
	 * @param shareNum : int
	 */
	public void no(Integer agentID, double shareNum) {
		this.no += shareNum;
	}
	
	/*
	 * How many shares does it take to get to the desired price
	 * @param desired price : double 
	 */
	public double howMany(double price, boolean direction) {
	  double top = !direction ? this.yes : this.no;
	  double side = direction ? this.yes : this.no;
	  double p = direction ? price : (1-price);
	  return this.b*Math.log((p*Math.exp(top/this.b))/(1-p)) - side;
	}
	
	/*
	 * How many shares does it take to fill this budget
	 */
	public double budgetToShares(double budget, boolean direction) {
		  double top = !direction ? this.yes : this.no;
		  double side = direction ? this.yes : this.no;
		  return this.b
	        * Math.log(Math.exp(budget
	            / this.b
	            + Math.log(Math.pow(Math.E, this.yes / this.b)
	                + Math.pow(Math.E, this.no / this.b)))
	            - Math.exp(top / this.b)) - side;
	}
	
	/*
	 * Allows the market maker to update b
	 */
	public void setB(double b) {
		this.b = b;
	}

	/**
	 * ID corresponds to type of PM this is; ie. what is this the question
	 * @return ID
	 */
	public Integer getId() {
		return this.ID;
	}
	
}
