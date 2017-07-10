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
import org.apache.commons.math3.random.RandomGenerator;

import brown.assets.value.FullType;

/**
 * valuation implementation where valuations follow a normal distribution
 * with mean bundle value determined by the value function.
 * TODO: positive semidefinite requirement for varCoVar
 * cut out the old value generator
 * connect the VCG from the old value generator.
 * @author acoggins
 *
 */
public class NormalValuation implements IValuation {
	private Set<FullType> goods; 
	private Function<Integer, Double> valFunction; 
	private Double baseVariance; 
	private Double expectedCovariance;
	private Boolean isMonotonic; 
	private Double valueScale;
	private Double varCoVar[][];

	
	/**
	 * Normal Valuation constructor. 
	 * @param goods
	 * a set of fulltype to be given values.
	 * @param valFunction
	 * a function from bundle size to positive reals by which valuation means are determined. 
	 * @param isMonotonic
	 * is every set of goods weakly dominant to all its subsets? 
	 * @param valueScale
	 * coefficient of scale.
	 */
	public NormalValuation (Set<FullType> goods, Function<Integer, Double> valFunction, 
			 Boolean isMonotonic, Double valueScale) {
		this.goods = goods; 
		this.valFunction = valFunction; 
		this.baseVariance = 1.0;
		this.expectedCovariance = 0.0;
		this.isMonotonic = isMonotonic; 
		this.valueScale = valueScale;	
	}
	
	/**
	 * Normal valuation constructor with variance covariance controls.
	 * @param goods
	 *  a set of FullType to be given values.
	 * @param valFunction
	 * a function from bundle size to positive reals by which valuation means are determined. 
	 * @param baseVariance
	 * the independent variance of a good's price.
	 * @param expectedCovariance
	 * the expected covariance between goods.
	 * @param isMonotonic
	 * is every set of goods weakly dominant to all its subsets? 
	 * @param valueScale
	 * coefficient of scale.
	 */
	public NormalValuation (Set<FullType> goods, Function<Integer, Double> valFunction, 
			Double baseVariance, Double expectedCovariance, Boolean isMonotonic, 
			Double valueScale) {
		this.goods = goods; 
		this.valFunction = valFunction; 
		this.expectedCovariance = expectedCovariance; 
		this.isMonotonic = isMonotonic; 
		this.valueScale = valueScale;	
	}
	

	@Override
	public ValuationBundle getAllValuations() {
	  populateVarCoVarMatrix();
		//random generator for all distributions in this method
		RandomGenerator rng = new ISAACRandom();
		Map<Map<Integer, FullType>, Double> existingSetsID =
				new HashMap<Map<Integer, FullType>, Double>();
		//map to cater to necessary iteration structure for monotonicity
		Map<Map<Integer, FullType>, Double> previousSize =
				new HashMap<Map<Integer, FullType>, Double>();
 		Map<Integer, FullType> numberGoods = new HashMap<Integer, FullType>();
 		int count = 0; 
 		for (FullType good : goods) {
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
							if (!temp.containsKey(eCopy)) {
							Double totalVariance = 0.0; 
							for(int anId : eCopy.keySet()) {
								for(int secondId : eCopy.keySet()) {
									totalVariance += varCoVar[anId][secondId];
								}
							}
							Double bundleMean = valFunction.apply(eCopy.size()) * valueScale; 
							NormalDistribution bundleDist = new NormalDistribution(rng, bundleMean,
									totalVariance);
							if (!isMonotonic) {
								temp.put(eCopy, bundleDist.sample());
							}
							else {
								//apply monotonic constraints. 
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
								Double sampledValue = -0.1;
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
		}
		//move the existing sets from an ID based to the structure in the type signature. 
		ValuationBundle existingSets = new ValuationBundle();
		for(Map<Integer, FullType> idGood : existingSetsID.keySet()) {
			Set<FullType> goodsToReturn = new HashSet<FullType>(idGood.values());
			existingSets.add(goodsToReturn, existingSetsID.get(idGood));
		}
		return existingSets;
		}
	
	@Override
	public ValuationBundle getSomeValuations(Integer numberOfValuations, 
			Integer bundleSizeMean, Double bundleSizeStdDev) {
	  if (bundleSizeMean > 0 && bundleSizeStdDev > 0) {
	    populateVarCoVarMatrix();
	    RandomGenerator rng = new ISAACRandom();
	    NormalDistribution sizeDist = new NormalDistribution(rng, bundleSizeMean, 
	        bundleSizeStdDev);
	  ValuationBundle valuations = new ValuationBundle();
	  for(int i = 0; i < numberOfValuations; i++) {
	      Boolean reSample = true;
        while(reSample) {
          int size = -1; 
          //repeatedly sample bundle size until a valid size is picked.
          while (size < 1 || size > goods.size()) {
            size = (int) sizeDist.sample();}
          Map<Integer, FullType> theGoods = new HashMap<>();
          Set<FullType> goodsSet = new HashSet<>();
          //list of goods to uniformly sample from
            List<FullType> goodList = new ArrayList<FullType>(goods); 
            //sample without replacement goods to add to the bundle size times.
            for(int j = 0; j < size; j++) {
              Integer rand = (int) (Math.random() * goodList.size());
              FullType aGood = goodList.get(rand);
              theGoods.put(rand, aGood);
              goodsSet.add(aGood);
              goodList.remove(aGood);
            }
            if(!valuations.contains(goodsSet)) {
              reSample = false;
              Double variance = 0.0;
              for(Integer id : theGoods.keySet()) {
                for(Integer idTwo : theGoods.keySet()) {
                  variance += varCoVar[id][idTwo];
                }
              }
              NormalDistribution bundleDist = new NormalDistribution(rng,
                  valFunction.apply(theGoods.size()), 
                  variance);
              if(!isMonotonic) {
                valuations.add(new Valuation(goodsSet, bundleDist.sample()));
              }
              else {
                Double minimumPrice = 0.0;
                for(Valuation v : valuations) {
                if(isSubset(v.getGoods(), goodsSet) &&
                    v.getPrice() > minimumPrice) {
                    minimumPrice = v.getPrice();
                  }
                }
                Double bundlePrice = -0.1;
                while(bundlePrice < minimumPrice) {
                  bundlePrice = bundleDist.sample();
                }
                valuations.add(new Valuation(goodsSet, bundlePrice));
              }
            }  
          }
	      }
	  return valuations; 
	}
	else {
	  System.out.println("ERROR: bundle size parameters not positive");
    throw new NotStrictlyPositiveException(bundleSizeMean);
	}
	}

	/**
	 * Helper function for populating the variance covariance matrix
	 * for the above methods
	 */
	private void populateVarCoVarMatrix() {
    NormalDistribution varianceDist = new NormalDistribution(new ISAACRandom(),
    expectedCovariance, 0.1);
   varCoVar = new Double[goods.size()][goods.size()];
    for(int i = 0; i < goods.size(); i++) {
      for(int j = i; j < goods.size(); j++) {
        if (i == j) {
          varCoVar[i][j] = baseVariance; 
        }
        else {
          Double entry = Double.POSITIVE_INFINITY;
          while (Math.abs(entry) >= baseVariance) {
            entry = varianceDist.sample();
          }
          varCoVar[i][j] = entry;
          varCoVar[j][i] = entry;
        }
      }
    }
    }
	
  //Boolean isPositiveDefinite = false; 
  //check the positive definance of the matrix.
//  for(int i = 0; i < GOODS.size() - 1; i++) {
//    int offset = 1;
//    while (varCoVar[i][i] == 0 && (offset + i) < GOODS.size()) {
//      for(int j = 0; j < GOODS.size(); j++) {
//        Double tmp = varCoVar[i + offset][j];
//        varCoVar[i + offset][j] = varCoVar[i][j];
//        varCoVar[i][j] = tmp;
//      }
//      offset++;
//      if(isPositiveDefinite) {
//        isPositiveDefinite = false; 
//      }
//      else {
//        isPositiveDefinite = true;
//      }
//    }
//    if (varCoVar[i][i] < 0) {
//      for(int j = 0; j < GOODS.size(); j++) {
//        varCoVar[i][j] = varCoVar[i][j] * -1;
//      }
//      if(isPositiveDefinite) {
//        isPositiveDefinite = false; 
//      }
//      else {
//        isPositiveDefinite = true;
//      }
//    }
//    //now, we eliminate the rows below. 
//    for(int k = i + 1; k < GOODS.size(); k++) {
//      Double coefficient = varCoVar[k][i] / varCoVar[i][i];
//      for(int j = 0; j < GOODS.size(); j++) {
//        varCoVar[k][j] -= (varCoVar[i][j] * coefficient);
//      }
//    }
//    
//  }
//  Double product = 1.0;
//  for(int i = 0; i < GOODS.size(); i++) {
//    product = product * varCoVar[i][i];
//  }
//  if(product >= 0 && isPositiveDefinite) {
//  }
//  else if (product < 0 && !isPositiveDefinite) {
//    isPositiveDefinite = true;
//  }
//  else {
//    isPositiveDefinite = false; 
//  }
//  System.out.println("A");
//  }
  
  
  //give each good an ID
	
	/**
	 * Helper function, determines is the first set is a subset of the second.
	 * @param firstSet
	 * the first set for which we determine if it is a subset of the second
	 * @param secondSet
	 * the set that the first is being compared against. 
	 * @return
	 * whether or not the first set is a subset of the second.
	 */
	private Boolean isSubset(Set<FullType> firstSet, Set<FullType> secondSet) {
	  for(FullType f : firstSet) {
	    if(!secondSet.contains(f)) {
	      return false;
	    }
	  }
	  return true;
	}
}

