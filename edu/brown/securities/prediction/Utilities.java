package brown.securities.prediction;

import java.util.Arrays;

import brown.securities.mechanisms.lmsr.LMSRBackend;

public final class Utilities {
	
	public static double findBWeighted(Double[] values, Double[] budgets, 
			double weightedAverage, double yes, double no) {
		if (values.length != budgets.length) {
			throw new IllegalArgumentException();
		}
		
		double b = .001;
		LMSRBackend backend = newBackend(b, yes, no);
		double p = simulate(backend, values, budgets).price(true);
		while((p > weightedAverage + .0001 || p < weightedAverage - .0001) && b < 10000) {
			b += .001;
			backend = newBackend(b, yes, no);
			p = simulate(backend, values, budgets).price(true);
		}
		
		return b < 10000 ? b : -1;
	}

	public static double findB(Double[] values, Double[] budgets) {
		if (values.length != budgets.length) {
			throw new IllegalArgumentException();
		}
		
		double weightedAverage = 0;
		for (int i = 0; i < values.length; i++) {
			weightedAverage += values[i] * budgets[i];
		}
		weightedAverage /= Arrays.asList(budgets).stream().reduce(0.0, Double::sum);
		
		return findBWeighted(values, budgets, weightedAverage, 0,0);
	}
	
	private static LMSRBackend simulate(LMSRBackend backend, Double[] values, Double[] budgets) {
		for (int i = 0; i < values.length; i++) {
			boolean dir = values[i] > backend.price(true);
			double idealShareNum = backend.howMany(values[i], dir);
			double idealCost = dir ? backend.cost(idealShareNum, 0) : backend.cost(0, idealShareNum);
			double shareNum = idealCost > budgets[i] ? backend.budgetToShares(budgets[i], dir) : idealShareNum;
			if(dir) {
				backend.yes(i, shareNum);
			} else {
				backend.no(i, shareNum);
			}			
		}
		
		return backend;
	}
	
	private static LMSRBackend newBackend(double B, double yes, double no) {
		return new LMSRBackend(B,yes,no);
	}
	
	public static void main(String[] args) {
		Double[] values = new Double[]{.8,.7,.6};
		Double[] budgets = new Double[]{1.0,1.0,1.0};
		System.out.println(Utilities.findB(values, budgets));
	}
}
