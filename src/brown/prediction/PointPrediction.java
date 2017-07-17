package brown.prediction;



public class PointPrediction implements IPricePrediction {

  private PredictionVector predictions;
  
  public PointPrediction(PredictionVector p) {
    this.predictions = p;
  }
  
  @Override
  public PredictionVector getPrediction() {
    return predictions;
  }

  
}
