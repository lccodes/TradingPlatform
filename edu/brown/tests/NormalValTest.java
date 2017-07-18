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
	private final NormalValuation nv = new NormalValuation(goods, linear, true, 1.0);
	
	public void testAllVals() {
		goods.add(new FullType(TradeableType.Good, 0));
		System.out.println(nv.getAllValuations());
	}
	
	public void testSomeVals() {
		for(int i = 0; i < 100; i++) {
		  goods.add(new FullType(TradeableType.Good, i));
		}

    System.out.println(nv.getSomeValuations(15, 22, 1.0));
	}
	
	public static void main(String[] args) {
		NormalValTest nv = new NormalValTest(); 
		nv.testAllVals();
	}
}

