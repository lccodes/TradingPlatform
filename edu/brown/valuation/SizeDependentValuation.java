package brown.valuation;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.ISAACRandom;

import brown.assets.value.BasicType;

/**
 * gets valuations where values directly depend on input size function.
 * @author acoggins
 *
 */
public class SizeDependentValuation implements IValuation {
	private Set<BasicType> goods; 
	private Function<Integer, Double> valFunction; 
	private Double valueScale;

	/**
	 * A Size Dependent Valuation constructor takes goods, a value function, and a value scale
	 * @param goods
	 * The set of fulltype that are given values. 
	 * @param valFunction
	 * The function that determines the relative values of the bundles. 
	 * @param valueScale
	 * Value scale that multiplies the value of all bundles.
	 */
	public SizeDependentValuation (Set<BasicType> goods, Function<Integer, Double> valFunction, 
			 Double valueScale) {
		this.goods = goods; 
		this.valFunction = valFunction; 
		this.valueScale = valueScale;	
	}
	
	/**
	 * gets all possible valuations for bundles of the input good.
	 */
	@Override
	public ValuationBundle getAllValuations() {
		//initialize a bundle of existing sets.
		ValuationBundle existingSets = new ValuationBundle();
		//add an empty bundle
		existingSets.add(new Valuation(new HashSet<BasicType>(), 0.0));
		  //iterate bundle adding process over all goods
			for(BasicType good : goods) {
			  //create temporary bundle to be added later. 
				ValuationBundle temp = new ValuationBundle();
				//for each value in existingSets, create a new bundle containing that good
				//for every bundle that does not contain that good.
				for(Valuation e : existingSets) {
					if (!e.contains(good)) {
					  //create a copy set of goods, and add the new good to it.
						Set<BasicType> eCopy = new HashSet<BasicType>(e.getGoods()); 
						eCopy.add(good);
						//add it to temp with a price determined by the value function.
						temp.add(eCopy, valFunction.apply(eCopy.size()) * valueScale);
					}
				}
				//add temp to existing sets
				existingSets.addAll(temp);
			}
		return existingSets;
	}
	
	/**
	 * gets some value bundles over the inputs.
	 */
	@Override
	public ValuationBundle getSomeValuations(Integer numberOfValuations, 
			Integer bundleSizeMean, Double bundleSizeStdDev) {
	  //check valid inputs for bundle size mean and std. dev.
		if (bundleSizeMean > 0 && bundleSizeStdDev > 0) {
		  //create a normal distribution to draw bundle sizes. 
			NormalDistribution sizeDist = new NormalDistribution(new ISAACRandom(), bundleSizeMean, 
				bundleSizeStdDev);
			//create a new bundle to add to.
			ValuationBundle existingSets = new ValuationBundle();
			//iterate over the number of valuations that are input
			for(int i = 0; i < numberOfValuations; i++) {
				Boolean reSample = true;
				//repeated sampling of bundles so no duplicate bundles appear in output.
				while(reSample) {
					int size = -1; 
					//repeatedly sample bundle size until a valid size is picked.
					while (size < 1 || size > goods.size()) {
						size = (int) sizeDist.sample();}
					//bundle to be added to
						Set<BasicType> theGoods = new HashSet<>();
					//list of goods to uniformly sample from
						List<BasicType> goodList = new ArrayList<BasicType>(goods); 
						//sample without replacement goods to add to the bundle size times.
						for(int j = 0; j < size; j++) {
							Integer rand = (int) (Math.random() * goodList.size());
							BasicType aGood = goodList.get(rand);
							theGoods.add(aGood);
							goodList.remove(aGood);
						}
						//if the resulting bundle is already in the existing bundles,
						//throw out and start over
						if(!existingSets.contains(theGoods)) {
							existingSets.add(theGoods, valFunction.apply(theGoods.size()) * valueScale);
							reSample = false;
						}
				}
			}
			return existingSets; 
	}
		//throw error if inputs for bundle size mean and standard deviation are not positive. 
		else {
			System.out.println("ERROR: bundle size parameters not positive");
			throw new NotStrictlyPositiveException(bundleSizeMean);
		}
	}


}
