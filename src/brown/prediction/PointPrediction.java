package brown.prediction;



public class PointPrediction implements IPricePrediction {

  private PredictionVector predictions;
  
  
  public PointPrediction() {
    this.predictions = new PredictionVector();
  }
  public PointPrediction(PredictionVector p) {
    this.predictions = p;
  }
  
  @Override
  public PredictionVector getPrediction() {
    return predictions;
  }
  
  @Override
  public void setPrediction(GoodPrice aPrediction) {
    this.predictions.add(aPrediction);
  }

  
}
