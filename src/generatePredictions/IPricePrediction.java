package generatePredictions;

import java.util.Map;

public interface IPricePrediction {
	
	public Map<Good,Price> getMeanPricePrediction();

	public Map<Good, Price> getRandomPricePrediction();

	public Map<Price,Double> normalize(Map<Price,Double> toNormalize);
	
	
}
