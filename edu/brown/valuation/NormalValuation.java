package brown.valuation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.ISAACRandom;
import org.apache.commons.math3.random.RandomGenerator;

import brown.assets.value.FullType;

public class NormalValuation implements Valuation{
	private Set<FullType> GOODS; 
	private Function<Integer, Double> VALFUNCTION; 
	private Double BASEVARIANCE; 
	private Double EXPECTEDCOVARIANCE;
	private Boolean MONOTONIC; 
	private Double VALUESCALE;

	
	
	public NormalValuation (Set<FullType> goods, Function<Integer, Double> valFunction, 
			 Boolean isMonotonic, Double valueScale) {
		this.GOODS = goods; 
		this.VALFUNCTION = valFunction; 
		this.BASEVARIANCE = 1.0;
		this.EXPECTEDCOVARIANCE = 0.0;
		this.MONOTONIC = isMonotonic; 
		this.VALUESCALE = valueScale;	
	}
	
	//constructor with variance controls
	public NormalValuation (Set<FullType> goods, Function<Integer, Double> valFunction, 
			Double baseVariance, Double expectedCovariance, Boolean isMonotonic, 
			Double valueScale) {
		this.GOODS = goods; 
		this.VALFUNCTION = valFunction; 
		this.EXPECTEDCOVARIANCE = expectedCovariance; 
		this.MONOTONIC = isMonotonic; 
		this.VALUESCALE = valueScale;	
	}
	
	@Override
	public Map<Set<FullType>, Double> getTotalValuation() {
		//random generator for all distributions in this method
		RandomGenerator rng = new ISAACRandom();
		Map<Map<Integer, FullType>, Double> existingSetsID =
				new HashMap<Map<Integer, FullType>, Double>();
		//populate variance covariance matrix
		Double varCoVar[][] = new Double[GOODS.size()][GOODS.size()];
		NormalDistribution varianceDist = new NormalDistribution(rng,
				EXPECTEDCOVARIANCE, 1.0);
		for(int i = 0; i < GOODS.size(); i++) {
			for(int j = i; j < GOODS.size(); j++) {
				if (i == j) {
					varCoVar[i][j] = BASEVARIANCE; 
				}
				else {
					Double entry = varianceDist.sample();
					varCoVar[i][j] = entry;
					varCoVar[j][i] = entry;
				}
			}
		}
		//give each good an ID
 		Map<Integer, FullType> numberGoods = new HashMap<Integer, FullType>();
 		int count = 0; 
 		for (FullType good : GOODS) {
 			numberGoods.put(count, good);
 		}
		existingSetsID.put(new HashMap<Integer, FullType>(), 0.0);
		//pupulate bundle set
		for(Integer id : numberGoods.keySet()) {
			Map<Map<Integer, FullType>, Double> temp =
					new HashMap<Map<Integer, FullType>, Double>();
			for(Map<Integer, FullType> e : existingSetsID.keySet()) {
				if (!e.keySet().contains(id)) {
					Map<Integer, FullType> eCopy = new HashMap<Integer, FullType>(e);
					eCopy.put(id, numberGoods.get(id));
					Double totalVariance = 0.0; 
					for(int anId : eCopy.keySet()) {
						for(int secondId : eCopy.keySet()) {
							totalVariance += varCoVar[anId][secondId];
						}
					}
					Double bundleMean = VALFUNCTION.apply(eCopy.size()) * VALUESCALE; 
					NormalDistribution bundleDist = new NormalDistribution(rng, bundleMean,
							totalVariance);
					if (!MONOTONIC) {
						temp.put(eCopy, bundleDist.sample());
					}
					else {
						//apply monotonic constraints. 
						//NOTE ONLY WORKS WHEN ALL BUNDLES OF SIZE X ARE CONSTRUCTED BEFORE
						//ALL BUNDLES SIZED X + 1
						Map<Map<Integer, FullType>, Double> directSubsets = 
							new HashMap<Map<Integer, FullType>, Double>();
						Double highestValSubSet = 0.0;
						for(Integer anId : eCopy.keySet()) {
							Map<Integer, FullType> eCopyCopy = 
									new HashMap<Integer, FullType>(eCopy);
							eCopyCopy.remove(anId);
							if(existingSetsID.containsKey(eCopyCopy)) {
								if(existingSetsID.get(eCopyCopy) > highestValSubSet) {
									highestValSubSet = existingSetsID.get(eCopyCopy);
								}
							}
						}
						Double sampledValue = 0.0;
						while (sampledValue < highestValSubSet) {
							sampledValue = bundleDist.sample();
						}
						temp.put(eCopy, sampledValue);
					}
				}
				existingSetsID.putAll(temp);
			}
		}
		Map<Set<FullType>, Double> existingSets = new HashMap<Set<FullType>, Double>();
		for(Map<Integer, FullType> idGood : existingSetsID.keySet()) {
			existingSets.put((Set) idGood.values(), existingSetsID.get(idGood));
		}
		return existingSets;
		}
	
	@Override
	public Map<Set<FullType>, Double> getValuation(Integer numberOfvaluations, 
			Integer bundleSizeMean, Double bundleSizeStdDev) {
		return null; 
	}


}

