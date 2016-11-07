package brown.test.prediction;

import brown.securities.SecurityFactory;
import brown.securities.prediction.PMBackend;
import brown.securities.prediction.PMTriple;

/*
 * It appears that for all bidders and all budgets and all orderings there exists
 * a B s.t. LMSR simulates a weighted average
 */
public final class PredictionTest {
	private static BidderFactory factory = new BidderFactory();
	static {
		factory.addBidder(.1, 10);
		factory.addBidder(.8, 10);
		factory.addBidder(.9, 10);
		factory.addBidder(.99, 10);
		//factory.shuffle();
	}
	
	private static void runTests() {
		for (float B = 1; B < 99; B+=1) {
			PMTriple triple = SecurityFactory.makePM(1, 2, B);
			PMBackend backend = triple.backend;
			
			for (Bidder bidder : factory.getBidders()) {
				boolean dir = bidder.value > backend.price(true);
				double idealShareNum = backend.howMany(bidder.value, dir);
				double idealCost = dir ? backend.cost(idealShareNum, 0) : backend.cost(0, idealShareNum);
				double shareNum = idealCost > bidder.budget ? backend.budgetToShares(bidder.budget, dir) : idealShareNum;
				if(dir) {
					backend.yes(shareNum);
				} else {
					backend.no(shareNum);
				}
				//System.out.println("ideal : " + idealShareNum + ", shares: " + shareNum + ", direction: " + dir + ", new price: " + backend.price(true));	
			}
			
			//System.out.println("final price " + backend.price(true));
			//System.out.println("average " + factory.getAverage());
			System.out.println(B + "," + backend.price(true));
		}
		System.out.println("average " + factory.getAverage());
	}
	
	public static void main(String[] args) {
		PredictionTest.runTests();
	}

}
