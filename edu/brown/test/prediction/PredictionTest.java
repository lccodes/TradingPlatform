package brown.test.prediction;

import brown.securities.SecurityFactory;
import brown.securities.prediction.PMTriple;
import brown.securities.prediction.structures.PMBackend;

/*
 * It appears that for all bidders and all budgets and all orderings there exists
 * a B s.t. LMSR simulates a weighted average
 */
public final class PredictionTest {

	private static double[] values = new double[]{.9,.9,.9,.1};
	private static double[] budgets = new double[]{1,1,1,10};
	private static double B = 1;
	
	private static void runTests() {
		PMTriple triple = SecurityFactory.makePM(1, 2, B);
		PMBackend backend = triple.backend;
		
		for (int i = 0; i < values.length; i++) {
			if (i == values.length-1) {
				backend.setB(13);
			}
			boolean dir = values[i] > backend.price(true);
			double idealShareNum = backend.howMany(values[i], dir);
			double idealCost = dir ? backend.cost(idealShareNum, 0) : backend.cost(0, idealShareNum);
			double shareNum = idealCost > budgets[i] ? backend.budgetToShares(budgets[i], dir) : idealShareNum;
			//double oldPrice = backend.price(true);
			double cost = dir ? backend.cost(shareNum, 0) : backend.cost(0, shareNum);
			if(dir) {
				backend.yes(i, shareNum);
			} else {
				backend.no(i, shareNum);
			}
			System.out.println("cost : " + cost + ", shares: " + shareNum + ", direction: " + dir + ", new price: " + backend.price(true));
			
			//System.out.println("final price " + backend.price(true));
			//System.out.println("average " + factory.getAverage());
			//System.out.println(B + "," + backend.price(true));
		}
	}
	
	public static void main(String[] args) {
		PredictionTest.runTests();
	}

}
