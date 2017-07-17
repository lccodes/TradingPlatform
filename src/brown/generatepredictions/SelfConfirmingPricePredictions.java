package brown.generatepredictions;

import java.util.Map;
import java.util.Set;

import Prediction.IPricePrediction;

public class SelfConfirmingPricePredictions implements IPricePrediction {

	private Double[] _price;

	public SelfConfirmingPricePredictions() {

	}

	public Double[] generatePredictions(int K, int L, int tau, IPredictionStrategy strat){
		Double[] price= new Double[strat.goodSize()];
		for (int t=0; t<L; t++){
		  //p=this.pricePredictionStrategy(p);
			Map<Set<FullType>, Double> valuations = new Map<Set<FullType>, Double>;
			strat = new secondPriceStrategy(valuations);
			Double[] p = strat.getPrediction();
			if (p==price){
				_price=price;
				return price;
			}
			double k= (L-tau +1)/L;
			for (int i=0; i<price.length; i++){ 
				price[i]=k*p[i]+(1-k)*price[i];
				double delta= Math.abs(price[i]-p[i]);
				if (delta<tau){ 
					_price=price;
					return price;
				}
			}
		}
		_price=price;
		return price;
	}

	@Override
	public Double[] getPricePrediction() {
		return _price;
	}

}
