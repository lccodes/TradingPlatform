package generatePredictions;

public class GeneratePredictions {
	
	public GeneratePredictions(){
		this.generatePredictions(1,1,10);
	}
	
	public double generatePredictions(int K, int L, int tau){
		double price=0;
		double p = 100;
		for (int t=0; t<L; t++){
			p=this.pricePredictionStrategy(p);
			if (p==price){
				return price;
			}
			double k= (L-tau +1)/L;
			price=k*p+(1-k)*price;
			double delta= Math.abs(price-p);
			if (delta<tau){
				return price;
			}
		}
		return price;
	}
	
	public double LocalBid(double pricePrediction, double strategy, double k){
		double b=strategy;
		for (int i=0; i<k; i++ ){
			for (int j=0; j<8; j++){ //8 is the number of auctioned goods
				
			}
		}
		return b;	
	}
	
	
	public double pricePredictionStrategy(double preferance){
		double p=preferance+10;
		return p;
	}
}
