package brown.tests; 

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import brown.assets.value.FullType;
import brown.assets.value.TradeableType;
import brown.valuation.NormalValuation;

public class NormalValTest {
	
	private final Set<FullType> goods = new HashSet<FullType>();
	private final Set<FullType> goodsTwo = new HashSet<FullType>();
	private final Function<Integer, Double> linear = x -> (double) x; 
	private final NormalValuation nv = new NormalValuation(goodsTwo, linear, true, 1.0);
	
	public void testAllVals() {
		goods.add(new FullType(TradeableType.Good, 0));
		goods.add(new FullType(TradeableType.Good, 1));
		goods.add(new FullType(TradeableType.Good, 2));
    goods.add(new FullType(TradeableType.Good, 3));
    goods.add(new FullType(TradeableType.Good, 4));
    goods.add(new FullType(TradeableType.Good, 5));
    goods.add(new FullType(TradeableType.Good, 6));
    goods.add(new FullType(TradeableType.Good, 7));
    goods.add(new FullType(TradeableType.Good, 8));
    goods.add(new FullType(TradeableType.Good, 9));
		System.out.println(nv.getAllValuations());
	}
	
	public void testSomeVals() {
		goodsTwo.add(new FullType(TradeableType.Good, 0));
		goodsTwo.add(new FullType(TradeableType.Good, 1));
    goodsTwo.add(new FullType(TradeableType.Good, 2));
    goodsTwo.add(new FullType(TradeableType.Good, 3));
    System.out.println(nv.getSomeValuations(5, 2, 1.0));
	}
	
	public static void main(String[] args) {
		NormalValTest nv = new NormalValTest(); 
		nv.testSomeVals();
	}
}

