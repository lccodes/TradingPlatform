package brown.tests;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import brown.assets.value.BasicType;
import brown.assets.value.TradeableType;
import brown.valuation.SizeDependentValuation;


public class PointValTest {
	private final Set<BasicType> fullSet = new HashSet<>(); 
	

	private final Function<Integer, Double> root = x -> Math.sqrt(x); 
	private final Function<Integer, Double> square = x -> (double) x * x; 
	private final SizeDependentValuation sdvRoot = new SizeDependentValuation(fullSet, root ,1.0);
	private final SizeDependentValuation sdvSquare = new SizeDependentValuation(fullSet, square ,1.0);
	
	public void testVal() {
		fullSet.add(new BasicType(TradeableType.Good, 0));
		fullSet.add(new BasicType(TradeableType.Good, 1));
		fullSet.add(new BasicType(TradeableType.Good, 2));
		fullSet.add(new BasicType(TradeableType.Good, 3));
    fullSet.add(new BasicType(TradeableType.Good, 4));
    fullSet.add(new BasicType(TradeableType.Good, 5));
    fullSet.add(new BasicType(TradeableType.Good, 6));
    fullSet.add(new BasicType(TradeableType.Good, 7));
    fullSet.add(new BasicType(TradeableType.Good, 8));
    fullSet.add(new BasicType(TradeableType.Good, 9));
    fullSet.add(new BasicType(TradeableType.Good, 10));
    //fullSet.add(new FullType(TradeableType.Good, 11));
    //fullSet.add(new FullType(TradeableType.Good, 12));
    //fullSet.add(new FullType(TradeableType.Good, 13));


		//fullSet.add(new FullType(TradeableType.Good, 3));
		System.out.println(sdvRoot.getAllValuations());
	}
	public void testVal2() {
//		fullSet.add(new FullType(TradeableType.Good, 0));
//		fullSet.add(new FullType(TradeableType.Good, 1));
//		fullSet.add(new FullType(TradeableType.Good, 2));

		//fullSet.add(new FullType(TradeableType.Good, 3));
		System.out.println(sdvSquare.getAllValuations());
	}
	
	public void testPartialVal() {
		fullSet.add(new BasicType(TradeableType.Good, 0));
		fullSet.add(new BasicType(TradeableType.Good, 1));
		fullSet.add(new BasicType(TradeableType.Good, 2));
		fullSet.add(new BasicType(TradeableType.Good, 3));
		System.out.println(sdvRoot.getSomeValuations(4, 2, 1.0));
	}

	public static void main(String[] args){
		PointValTest pv = new PointValTest();
		pv.testVal();
		//pv.testVal2();
		//pv.testPartialVal();
		
	}
}