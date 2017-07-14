package generatePredictions;

import java.util.Set;

public class ValRandGenerator implements IValuationGenerator {

	@Override
	public double makeValuation(Good good) {
		return (Math.random()*Constants.MAX_VAL +Constants.MIN_VAL);
	}

	@Override
	public double makeValuation(Set<Good> goods) {
		return (Math.random()*Constants.MAX_VAL + Constants.MIN_VAL);
	}

}
