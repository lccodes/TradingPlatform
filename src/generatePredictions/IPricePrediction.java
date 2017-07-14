package generatePredictions;

import java.util.Map;

public interface IPricePrediction {
	
	public Map<Good,Price> getMeanPricePrediction();

	public Map<Good, Price> getRandomPricePrediction();
	
	
}
