package brown.tests; 

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import brown.assets.value.BasicType;
import brown.assets.value.TradeableType;
import brown.valuation.library.NormalValuation;

public class NormalValTest {
	
	private final Set<BasicType> goods = new HashSet<BasicType>();
	private final Set<BasicType> goodsTwo = new HashSet<BasicType>();
	private final Function<Integer, Double> linear = x -> (double) x; 
	private final NormalValuation nv = new NormalValuation(goods, linear, true, 1.0);
	
	public void testAllVals() {
		goods.add(new BasicType(TradeableType.Good, 0));
		System.out.println(nv.getAllValuations());
	}
	
	public void testSomeVals() {
		for(int i = 0; i < 100; i++) {
		  goods.add(new BasicType(TradeableType.Good, i));
		}

    System.out.println(nv.getSomeValuations(15, 22, 1.0));
	}
	
	public static void main(String[] args) {
		NormalValTest nv = new NormalValTest(); 
		nv.testAllVals();
	}
}

