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
	public Map<Set<FullType>, Double> getAllValuations() {
		//random generator for all distributions in this method
		RandomGenerator rng = new ISAACRandom();
		Map<Map<Integer, FullType>, Double> existingSetsID =
				new HashMap<Map<Integer, FullType>, Double>();
		//map to cater to necessary iteration structure for monotonicity
		Map<Map<Integer, FullType>, Double> previousSize =
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
					Double entry = Double.POSITIVE_INFINITY;
					while (Math.abs(entry) >= BASEVARIANCE) {
					 entry = varianceDist.sample();
					}
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
 			count++;
 		}
 		//give maps starting values
		existingSetsID.put(new HashMap<Integer, FullType>(), 0.0);
		previousSize.put(new HashMap<Integer, FullType>(), 0.0);
		for(int i = 0; i < numberGoods.size(); i++) {
			//hashmap populated with every subset of size i;
			Map<Map<Integer, FullType>, Double> temp =
					new HashMap<Map<Integer, FullType>, Double>();
			//for each good in the previous size subset
			for(Map<Integer, FullType> e : previousSize.keySet()) {
					//for each good, create a new bundle, as 
					for(Integer id : numberGoods.keySet()){
						Map<Integer, FullType> eCopy = new HashMap<Integer, FullType>(e);
						if(!e.keySet().contains(id)) {
							eCopy.put(id, numberGoods.get(id));
							System.out.println(eCopy);
							if (!temp.containsKey(eCopy)) {
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
						}
					}
				}
			existingSetsID.putAll(temp);
			previousSize = temp;
			//System.out.println(previousSize);
		}
		//move the existing sets from an ID based to the structure in the type signature. 
		Map<Set<FullType>, Double> existingSets = new HashMap<Set<FullType>, Double>();
		for(Map<Integer, FullType> idGood : existingSetsID.keySet()) {
			Set<FullType> goodsToReturn = new HashSet<FullType>(idGood.values());
			existingSets.put(goodsToReturn, existingSetsID.get(idGood));
		}
		return existingSets;
		}
	
	@Override
	public Map<Set<FullType>, Double> getValuation(Integer numberOfvaluations, 
			Integer bundleSizeMean, Double bundleSizeStdDev) {
		return null; 
	}


}

