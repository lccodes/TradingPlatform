package brown.valuation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.ISAACRandom;

import brown.assets.value.FullType;

public class SizeDependentValuation implements IValuation {
	private Set<FullType> GOODS; 
	private Function<Integer, Double> VALFUNCTION; 
	private Double VALUESCALE;

	
	public SizeDependentValuation (Set<FullType> goods, Function<Integer, Double> valFunction, 
			 Double valueScale) {
		this.GOODS = goods; 
		this.VALFUNCTION = valFunction; 
		this.VALUESCALE = valueScale;	
	}
	
	@Override
	public Map<Set<FullType>, Double> getAllValuations() {
		
		Map<Set<FullType>, Double> existingSets = new HashMap<Set<FullType>, Double>();
		existingSets.put(new HashSet<FullType>(), 0.0);
			for(FullType good : GOODS) {
				Map<Set<FullType>, Double> temp = new HashMap<Set<FullType>, Double>();
				for(Set<FullType> e : existingSets.keySet()) {
					if (!e.contains(good)) {
						Set<FullType> eCopy = new HashSet<FullType>(e); 
						eCopy.add(good);
						temp.put(eCopy, VALFUNCTION.apply(eCopy.size()) * VALUESCALE);
					}
				}
				existingSets.putAll(temp);
			}
		return existingSets;
	}
	
	@Override
	public Map<Set<FullType>, Double> getSomeValuations(Integer numberOfValuations, 
			Integer bundleSizeMean, Double bundleSizeStdDev) {
		//check that input parameters are positive
		if (bundleSizeMean > 0 && bundleSizeStdDev > 0) {
		//create distribution for bundle size drawing.
			NormalDistribution sizeDist = new NormalDistribution(new ISAACRandom(), bundleSizeMean, 
				bundleSizeStdDev);
			Map<Set<FullType>, Double> existingSets = new HashMap<Set<FullType>, Double>();
			for(int i = 0; i < numberOfValuations; i++) {
				//resample if the generated set has values that already exist
				Boolean reSample = true;
				while(reSample) {
					int size = -1; 
					while (size < 1 || size > GOODS.size()) {
						size = (int) sizeDist.sample();}
						Set<FullType> theGoods = new HashSet<>();
						List<FullType> goodList = new ArrayList<FullType>(GOODS); 
						for(int j = 0; j < size; j++) {
							Integer rand = (int) (Math.random() * goodList.size());
							FullType aGood = goodList.get(rand);
							theGoods.add(aGood);
							goodList.remove(aGood);
						}
						if(!existingSets.keySet().contains(theGoods)) {
							existingSets.put(theGoods, VALFUNCTION.apply(theGoods.size()) * VALUESCALE);
							reSample = false;
						}
				}
			}
			return existingSets; 
	}
		//if initial parameters not positive, throw an exception.
		else {
			System.out.println("ERROR: bundle size parameters not positive");
			throw new NotStrictlyPositiveException(bundleSizeMean);
		}
	}


}
