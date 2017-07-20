package brown.generatepredictions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import brown.interfaces.IIndependentPrediction;

public class IndHistogram implements IIndependentPrediction {

private Double[][] _histogram;
private Map<Good, Map<Price, Double>> _hist;
private Set<Price> _priceSet;
	public IndHistogram(int numGoods){
		// _histogram = new Double[(int) Constants.NUM_GOODS][(int) Constants.NUM_PRICES];
		  _priceSet = new HashSet<Price>();
		 double range =Constants.MAX_VAL- Constants.MIN_VAL;
		 double step =(range +1.0)/(double)Constants.NUM_PRICES;
		 for(int j=1; j<Constants.NUM_PRICES; j++){
			 Math.round(_priceSet.add(Constants.MIN_VAL +j*step));
		 }
		 for (double i=Constants.MIN_VAL; i<Constants.MAX_VAL +1; i+=step ){
			 _priceSet.add(j);
		 }
		 for(Good good:Constants.goodSet){
			 Map<Price, Double> initialCounts = new HashMap<Price, Double>();
			 for (Price price:_priceSet){
				 initialCounts.put(price, 1.0);
			 }
			 _hist.put(good,initialCounts);
		 }
	}
	
	public Price getBucket(Good g, double bid){
		return Math.max(Constants.MIN_VAL, Math.min(Constants.MAX_VAL, Math.round(bid)));
	}
	
	public void incCount(Good good, Price price){
		_hist.put(good, put(price,_hist.get(good).get(price)+1));
	}
	
	
	
	public Map<Price, Double> normalize(Map<Price, Double> toNormalize){
		Map<Price, Double> normalized = new HashMap<Price,Double>();
		double total =0.0;
		for(Price price:_priceSet){
			total+=toNormalize.get(price);
		}
		for(Price price:_priceSet){
			normalized.put(price, toNormalize.get(price)/total);
		}
		return normalized;
	}

	@Override
	public Map<Good, Price> getMeanPricePrediction() {
		Map<Good, Price> meanPricePrediction = new HashMap<Good, Price>();
		Set<Good> good = _hist.keySet();
		Iterator<Good> goodIterator = good.iterator();
		while (goodIterator.hasNext()){
			Good nextGood = goodIterator.next();
			Map<Price, Double> priceSet = _hist.get(nextGood);
			Iterator<Price> priceIterator=priceSet.keySet().iterator();
			Price mean;
			while(priceIterator.hasNext()){
				Price nextPrice = priceIterator.next();
				mean += nextPrice * priceSet.get(nextPrice); //Need to associate Price with Double
				priceIterator.remove(); //Check whether this line is necessary
			}
			meanPricePrediction.put(nextGood, mean);
			goodIterator.remove(); //Check whether this line is necessary.
		}
		return meanPricePrediction;
	}



	@Override
	public Map<Good, Price> getRandomPricePrediction() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Iterator<T> iterator(){
		return new Iterator<T>(){
			@Override
			public boolean hasNext(){
				return (_hist.hasNext());
			}
			@Override
			public <Map<Price, double>>next(){
				return (_hist.next());
			}
			@Override
			public void remove(){
				
			}
		};
	}
	/*
	 * This methods returns the distribution of probabilities for each good in the form of
	 * an array.
	 
		@Override
		public Double[] getDistPerGood(int goodNumber) {
			Double[] distributionPerGood = new Double[(int) Constants.NUM_PRICES];
			for (int j=0; j<Constants.NUM_PRICES; j++){
				distributionPerGood[j] =_histogram[goodNumber][j];
			}
			return distributionPerGood;
		}

	 * This method returns the mean price per good, as calculated based on the probabilities 
	 * drawn from the histogram for each good. For each good the probability for each price is 
	 * multiplied with the value of the price itself and then added to the mean. 
	 
		@Override
		public Double[] getMeanPerGood() {
			Double[] meanPerGood = new Double[Constants.NUM_GOODS];
			for (int i=0; i< Constants.NUM_GOODS; i++){
				for (int j=0; j<Constants.NUM_PRICES; j++){
					meanPerGood[i] +=_histogram[i][j] * j; 
				}
			}
			return meanPerGood;
		}

		@Override
		public Double getRandomSample(int goodNumber) {
			int sumOfProbs = 0;
			ArrayList<Double> list = new ArrayList<Double>();
			for (int j=0; j<Constants.NUM_PRICES; j++){
				sumOfProbs =(int) (_histogram[goodNumber][j]*10);
				for (int k=0; k<sumOfProbs; k++){
					list.add((double) j);
				}
			}
			return list.get((int) (Math.random()*list.size()));
		}
		*/

}
