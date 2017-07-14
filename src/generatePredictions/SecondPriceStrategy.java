package generatePredictions;

import java.util.Map;
import java.util.Set;

public class SecondPriceStrategy implements IPredictionStrategy {
private Double[] _estimatePrices;

	public SecondPriceStrategy(Map<Set<FullType>, Double> valuations){
		Double[] estimatePrices = new Double[valuations.size()];
		for (int i=0; i<valuations.size(); i++){
			estimatePrices[i]=valuations.values().iterator().next() - 5; 
			//Possible error due to the way iterator is used. Might need to make a new 
			//iterator variable and call .next() on the variable. 
			_estimatePrices=estimatePrices; 
		}
	}
	
	@Override
	public Double[] getPrediction() {
		return _estimatePrices;
	}
	
	@Override
	public int goodSize(){
		int length = _estimatePrices.length;
		return length;
	
	}

}
