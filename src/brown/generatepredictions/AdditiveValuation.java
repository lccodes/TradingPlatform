package brown.generatepredictions;

import java.util.Map;
import java.util.Set;

import brown.interfaces.IValuation;
import brown.interfaces.IValuationGenerator;

public class AdditiveValuation implements IValuation {
	Map<Good, Value> vals =null;
	public AdditiveValuation(Set<Good> goods, ValRandGenerator valRandGenerator){
		for(Good good:goods){
			vals.put(good, valRandGenerator.makeValuation(good));
		}
	}
	
	public AdditiveValuation(IValuationGenerator valGenerator){
		for(Good good:goods){
			vals.put(good,valGenerator());
		}
	}
	
	@Override
	public double getValuation(Good g) {
		
		return vals.get(g);
	}
	@Override
	public double getValuation(Set<Good> goods) {
		double sum=0.0;
		for(Good good:goods){
			sum+=vals.get(good);
		}
		return 0;
	}
}
