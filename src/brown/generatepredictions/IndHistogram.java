package brown.generatepredictions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import brown.interfaces.IIndependentPrediction;
import brown.prediction.Good;
import brown.prediction.GoodPrice;
import brown.prediction.PredictionVector;

public class IndHistogram implements IIndependentPrediction {


private Map<Good, Map<Price, Double>> _hist;
private Set<Price> _priceSet;
	public IndHistogram(int numGoods){
		  _priceSet = new HashSet<Price>();
		 double range =Constants.MAX_VAL- Constants.MIN_VAL;
		 double step =(range +1.0)/(double)Constants.NUM_PRICES;
		 for(int j=1; j<Constants.NUM_PRICES; j++){
			 Price price= new Price((double)Math.round((Constants.MIN_VAL +j*step)));
			_priceSet.add(price);
		 }
		 
		 for (double i=Constants.MIN_VAL; i<Constants.MAX_VAL +1; i+=step ){
			 Price priceStep = new Price(i);
			 _priceSet.add(priceStep);
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
		Price bucketPrice= new Price(Math.max(Constants.MIN_VAL, Math.min(Constants.MAX_VAL, Math.round(bid))));
		return bucketPrice;
	}
	
	public void incCount(Good good, Price price){
		Map<Price, Double> innerMap = new HashMap<Price, Double>();
		innerMap.put(price, _hist.get(good).get(price)+1.0);
		_hist.put(good, innerMap);
	}
	
	
	@Override
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
	public Map<Good,Map<Price,Double>> getMap(){
		return _hist;
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
			Price mean = new Price(0.0);
			Double meanPrice = 0.0;
			while(priceIterator.hasNext()){
				Price nextPrice = priceIterator.next();
				meanPrice += nextPrice.getValue() * priceSet.get(nextPrice); 
				mean.setValue(meanPrice);
				priceIterator.remove(); //Check whether this line is necessary
			}
			meanPricePrediction.put(nextGood, mean);
			goodIterator.remove(); //Check whether this line is necessary.
		}
		return meanPricePrediction;
	}



	@Override
	public Map<Good, Price> getRandomPricePrediction() {
		Map<Good, Price> randomPricePredictionMap = new HashMap<Good,Price>();
		Double[] increasingProbs = new Double[_hist.size()];
		Set<Good> good = _hist.keySet();
		Iterator<Good> goodIterator = good.iterator();
		Map<Double, Price> increasedProbsMap = new HashMap<Double, Price>();
		while(goodIterator.hasNext()){
			Good nextGood = goodIterator.next();
			Map<Price, Double> priceSet = _hist.get(nextGood);
			Iterator<Price> priceIterator=priceSet.keySet().iterator();
			Price priceOne = priceIterator.next();
			increasingProbs[0]=priceOne.getValue();
			increasedProbsMap.put(priceOne.getValue(),priceOne);
			int counter =1;
			while(priceIterator.hasNext()){
				Price nextPrice=priceIterator.next();
				increasingProbs[counter]=nextPrice.getValue()+increasingProbs[counter-1];
				increasedProbsMap.put(increasingProbs[counter], nextPrice);
				counter+=1;
			}
			double randomSample =Math.random();
			Price randomPrice=null;
			int newCounter=1;
			if (randomSample > increasingProbs[0]){
				while (randomSample>increasingProbs[newCounter]){
					newCounter+=1;
				}
				randomPrice=increasedProbsMap.get(increasingProbs[counter]);
			}
			else{
				randomPrice=increasedProbsMap.get(increasingProbs[0]);
			}
			randomPricePredictionMap.put(nextGood,randomPrice);
		}
		return randomPricePredictionMap;
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

	@Override
	public PredictionVector getPrediction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrediction(GoodPrice aPrediction) {
		// TODO Auto-generated method stub
		
	}

}
