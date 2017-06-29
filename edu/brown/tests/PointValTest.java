package brown.tests;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import brown.assets.value.FullType;
import brown.assets.value.TradeableType;
import brown.tradeables.Lab8Good;
import brown.valuation.PointValuation;


public class PointValTest {
	Set<FullType> fullSet = new HashSet<>(); 
	

	Function<Integer, Double> linear = x -> x + 1.0; 
	PointValuation pv = new PointValuation(fullSet, linear ,1.0);
	
	public void testPoint() {
		fullSet.add(new FullType(TradeableType.Good, 0));
		fullSet.add(new FullType(TradeableType.Good, 1));
		fullSet.add(new FullType(TradeableType.Good, 2));
	}
}