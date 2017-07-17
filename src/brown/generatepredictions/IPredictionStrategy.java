package brown.generatepredictions;

import brown.prediction.IPricePrediction;

public interface IPredictionStrategy {

	public IPricePrediction getPrediction();
	
}
