package brown.securities.prediction;

/**
 * Private backend prediction market implementation
 * Agents are provided pointers to the public face
 * to prevent illegal modifications
 * @author lcamery
 *
 */
public class PMBackend {
	protected double yes;
	protected double no;
	protected final double b;
	
	public PMBackend() {
		this.yes = 0;
		this.no = 0;
		this.b = 1;
	}
	
	public PMBackend(double b) {
		this.yes = 0;
		this.no = 0;
		this.b = b;
	}
	
	/*
	 * Cost function
	 * @param qd1 : new quantity yes
	 * @param qd2 : new quantity no
	 * @return cost : double
	 */
	public double cost(int newq1, int newq2) {
		return b*Math.log(Math.pow(Math.E, (newq1 + yes)/b) + Math.pow(Math.E, (newq2+no)/b))
				- b*Math.log(Math.pow(Math.E, yes/b) + Math.pow(Math.E, no/b));
	}
	
	/*
	 * Quotes the cumulative price for a certain number of yes shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double bid(int shareNum) {
		return cost(shareNum, 0);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of no shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double ask(int shareNum) {
		return cost(0, shareNum);
	}
	
	/*
	 * Returns a share to an agent that buys yes
	 * @param shareNum : int
	 */
	public void yes(int shareNum) {
		this.yes += shareNum;
	}
	
	/*
	 * Returns a share to an agent that buys no
	 * @param shareNum : int
	 */
	public void no(int shareNum) {
		this.no += shareNum;
	}
	
	/*
	 * How many shares does it take to get to the
	 * @param desired price : double 
	 */
	public double howMany(double price, boolean direction) {
	  double top = !direction ? this.yes : this.no;
	  double side = direction ? this.yes : this.no;
    return this.b
        * Math.log(Math.exp(price
            / this.b
            + Math.log(Math.pow(Math.E, this.yes / this.b)
                + Math.pow(Math.E, this.no / this.b)))
            - Math.exp(top / this.b)) - side;
	}
	
}
