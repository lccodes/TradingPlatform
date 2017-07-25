package brown.interfaces;

import java.util.Map;

import brown.generatepredictions.Price;
import brown.prediction.GoodPrice;
import brown.prediction.PredictionVector;

public interface IPricePrediction {
	
	public PredictionVector getPrediction();
	
	public void setPrediction(GoodPrice aPrediction);


}
