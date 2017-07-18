package brown.prediction;

import java.util.Map;

import brown.generatepredictions.Price;

public interface IPricePrediction {
	
	public PredictionVector getPrediction();
	
	public void setPrediction(GoodPrice aPrediction);


}
