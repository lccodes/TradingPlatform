package brown.test.prediction;

import brown.securities.SecurityFactory;
import brown.securities.prediction.PMTriple;
import brown.securities.prediction.structures.PMBackend;

public final class PredictionTest {
	private static double[] values = new double[]{.8,.7,.6};
	private static double[] budgets = new double[]{1,1,1};
	private static double B = 2;
	
	private static void runTests() {
		PMTriple triple = SecurityFactory.makePM(1, 2, B);
		PMBackend backend = triple.backend;
		
		for (int i = 0; i < values.length; i++) {
			boolean dir = values[i] > backend.price(true);
			double idealShareNum = backend.howMany(values[i], dir);
			double idealCost = dir ? backend.cost(idealShareNum, 0) : backend.cost(0, idealShareNum);
			double shareNum = idealCost > budgets[i] ? backend.budgetToShares(budgets[i], dir) : idealShareNum;
			double oldPrice = backend.price(true);
			if(dir) {
				backend.yes(shareNum);
			} else {
				backend.no(shareNum);
			}
			System.out.println("dif : " + (backend.price(true)-oldPrice) + ", shares: " + shareNum + ", direction: " + dir + ", new price: " + backend.price(true));
			
		}
		
		System.out.println("final price " + backend.price(true));
	}
	
	public static void main(String[] args) {
		PredictionTest.runTests();
	}

}
