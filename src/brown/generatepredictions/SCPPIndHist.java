package brown.generatepredictions;

import java.util.Map;

import brown.interfaces.IBidStrategy;
import brown.interfaces.IPredictionStrategy;
import brown.interfaces.IPricePrediction;

public class SCPPIndHist implements IPredictionStrategy {
	 double alpha =0.1;
	 double eps=0.1;
	 int NUM_ITERATIONS=100;
	 int NUM_SAMPLES=1000;
	 IPricePrediction pricePrediction = null;
	 
	 public SCPPIndHist(IBidStrategy bs){
		 IPricePrediction pp = new IndHistogram(Constants.NUM_GOODS);
		 pp.normalize();
		 IPricePrediction temp = new IndHistogram(Constants.NUM_GOODS);
		 for (int i=1; i<NUM_ITERATIONS; i++){
			 temp.clear();
			 temp=populateTemp(temp, bs);
			 pp=this.smooth(pp,temp);
		 }
		 pricePrediction=pp;
	 }
	 private IndHistogram populateTemp(IPricePrediction pp, IBidStrategy bs){
		 IPricePrediction temp = new IndHistogram(Constants.NUM_GOODS);
		 for(int j=1; j<NUM_SAMPLES; j++){
			 for(int l=1; l<Constants.NUM_AGENTS; l++){
				 Map<Good,Price> bids =bs.getBids();//PSeudocode has bb.getBids(pp) 
				 for(Good good:bids.keySet()){
					 double priceValue =temp.getBucket(good, bids.get(good));
					 Price price = new Price(priceValue);
					 temp.incCount(good,price);
				 }
			 }
		 }
		 return temp.normalize();
	 }
	 
	 private IndHistogram smooth(IndHistogram pp, IndHistogram temp){
		 for(Good good:pp.getMap().keySet()){ //I changed this from pp.keySet() to pp.getMap().keySet()
			 for(Map<Price,Double> probs:pp.getMap().get(good)){
				 for(Price price:probs.keySet()){
					 double newProb =(1.0-alpha)*pp.getMap().get(good).get(price)+alpha*temp.getMap().get(good).get(price);
					 pp.put(good).put(price,newProb);
				 }
				 
			 }
		 }
		 return pp;
	 }
	 
	 private double testConvergnce(IndHistogram pp, IndHistogram temp){
		 Double error = Double.NEGATIVE_INFINITY;
		 Double totalError=0.0;
		 for(Map<Price,Double> probs:pp){
			 for(Price price:probs.keySet()){
				 error=max(error, abs(pp.get(good).get(price)-temp.get(good).get(price)));
			 }
			 totalError+=error;
		 }
		 totalError/=pp.size();
		 return totalError;
	 }
	 
	@Override
	public IPricePrediction getPrediction() {
		return pricePrediction;
	}
	
}
