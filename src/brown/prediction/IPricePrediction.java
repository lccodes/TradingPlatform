package brown.prediction;

import java.util.Map;

import brown.generatepredictions.Good;
import brown.generatepredictions.Price;

public interface IPricePrediction {
	
	public Map<Good,Price> getMeanPricePrediction();

	public Map<Good, Price> getRandomPricePrediction();
	
	
}
