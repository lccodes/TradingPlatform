package brown.tests;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import brown.assets.value.FullType;
import brown.assets.value.TradeableType;
import brown.valuation.PointValuation;


public class PointValTest {
	private final Set<FullType> fullSet = new HashSet<>(); 
	

	private final Function<Integer, Double> linear = x -> (double) x; 
	private final PointValuation pv = new PointValuation(fullSet, linear ,10.0);
	
	public void testVal() {
		fullSet.add(new FullType(TradeableType.Good, 0));
		fullSet.add(new FullType(TradeableType.Good, 1));
		fullSet.add(new FullType(TradeableType.Good, 2));
		//fullSet.add(new FullType(TradeableType.Good, 3));
		System.out.println(pv.getTotalValuation());
	}
	
	public void testPartialVal() {
		fullSet.add(new FullType(TradeableType.Good, 0));
		fullSet.add(new FullType(TradeableType.Good, 1));
		fullSet.add(new FullType(TradeableType.Good, 2));
		fullSet.add(new FullType(TradeableType.Good, 3));
		System.out.println(pv.getValuation(4, 2, 1.0));
	}

	public static void main(String[] args){
		PointValTest pv = new PointValTest();
		//pv.testVal();
		pv.testPartialVal();
		
	}
}