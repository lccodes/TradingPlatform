package brown.generatepredictions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import brown.interfaces.IBidStrategy;
import brown.interfaces.IPricePrediction;

public class LocalBid implements IBidStrategy {
	double slack = 0.0001;
	double NUM_ITERATIONS=100;
	int NUM_SAMPLES=1000;
	Map<Good,Price> bids=null;
	private Double[] _currentBid;
	
	public LocalBid(IPricePrediction pp) {
		bids=pp.getMeanPricePrediction();
		for (int i=1; i<NUM_ITERATIONS; i++){
			for(Good good:bids.keySet()){
				double marginalUtility=0.0;
				double totalMarginalUtility;
				for (int j=1; j<NUM_SAMPLES; j++){
					Map<Good,Price> prices =pp.getRandomPricePrediction();
					double totalPrice=0.0;
					Set<Good> winnings = new HashSet<Good>();
					for(Good g:prices.keySet()){
						if(bids.get(good).getValue()+slack >prices.get(good).getValue()){
							winnings.add(good);
							if(!good.equals(g)){
								totalPrice += prices.get(g).getValue();
							}
						}   
					}
					double valWithGood, valWithOutGood;
					valWithGood=val.get(winningsWithGood.add(good));
					valWitOutGood=val.get(winningsWithGood.remove(good));
					totalMarginalUtility+=(valWithGood-valWithOutGood)-totalPrice;
				}
				bids.put(good, totalMarginalUtility/(double)NUM_SAMPLES);
			}
			
		}
	}
	
	
	/*public Double[] createBid(IndHistogram pricePredictions,
			int k) {
		Double[] finalBid = pricePredictions.getMeanPerGood();
		Double tempSum= 0.0;
		int[] winningGoods = new int [Constants.NUM_GOODS];
		for (int i = 0; i < k; i++) {
			Double samplePriceVector = pricePredictions.getMeanPerGood(j);
			for (int j = 0; j < Constants.NUM_GOODS; i++) {
				//initialBid=Valuation of al  l goods - (valuation of all goods - j)
				if (samplePriceVector >= finalBid[j]){
					winningGoods[j] =  1;
				}
				else{
					winningGoods[j]=  0;
				}
				//Calculate Valuation for the winning goods.
				//Update the finalBid based on valuation.
			}
		}
		_currentBid=finalBid;
		return finalBid;
	}
	*/



	@Override
	public Map<Good, Price> getBids() {
		return bids;
		}
	}
