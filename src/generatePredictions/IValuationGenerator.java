package generatePredictions;

import java.util.Set;

public interface IValuationGenerator {
	public double makeValuation(Good good);
	public double makeValuation(Set<Good> goods);
}
