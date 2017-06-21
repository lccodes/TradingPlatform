package brown.securities.prediction.simulator;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import brown.markets.LMSRBackend;

/*
 * It appears that for all bidders and all budgets and all orderings there exists
 * a B s.t. LMSR simulates a weighted average
 */
public final class PredictionTest {
	
	public static int countSwitch(List<Boolean> directions) {
		int count = 0;
		boolean last = directions.get(0);
		for (boolean b : directions) {
			if (last != b) {
				count++;
			}
			last = b;
		}
		return count;
	}
	
	private static void runTests() {
		double B = 4;
		int len = 2;
		int x = 0;
		Double[] values = new Double[len];
		double[] budgets = new double[len];
		double lowest = Double.MAX_VALUE;
		double highest = Double.MIN_VALUE;
		
		double avg = 0;
		Random random = new Random();
		for (int i = 0; i < values.length; i++) {
			values[i] = random.doubles(0, 1).findFirst().getAsDouble();
			avg += values[i];
			budgets[i] = 1;
		}
		avg/= values.length;
		while(x < 10) {
			LMSRBackend backend = new LMSRBackend(0,B);
			List<Double> list = Arrays.asList(values);
			Collections.shuffle(list);
			values = (Double[]) list.toArray();
			
			
			List<Boolean> where = new LinkedList<Boolean>();
			List<Double> path = new LinkedList<Double>();
			//double sumCost = 0;
			for (int i = 0; i < values.length; i++) {
				boolean dir = values[i] > backend.price(true);
				where.add(dir);
				double idealShareNum = backend.howMany(values[i], dir);
				double idealCost = dir ? backend.cost(idealShareNum, 0) : backend.cost(0, idealShareNum);
				double shareNum = idealCost > budgets[i] ? backend.budgetToShares(budgets[i], dir) : idealShareNum;
				//double oldPrice = backend.price(true);
				if (shareNum < .00001) {
					continue;
				}
				
				//double cost = dir ? backend.cost(shareNum, 0) : backend.cost(0, shareNum);
				//sumCost += cost;
				if(dir) {
					backend.yes(i, shareNum);
				} else {
					backend.no(i, shareNum);
				}
				path.add(Math.floor(backend.price(true)*100)/100);
				//System.out.println("cost : " + cost + ", shares: " + shareNum 
					//	+ ", direction: " + dir + ", new price: " + backend.price(true));
			}
			
			double dif = Math.abs(avg - backend.price(true));
			if (lowest > dif) {
				lowest = dif;
			} else if (highest < dif) {
				highest = dif;
			}
			x += 1;
			System.out.println(path + " " + list);
		}
		System.out.println(lowest + " " + highest + " " + avg);
	}
	
	/**
	 * NOTES
	 * It's about the *marginal* traders. The last person can make it very accurate or harm the
	 * accuracy.
	 * @param args
	 */
	public static void main(String[] args) {
		PredictionTest.runTests();
	}

}
