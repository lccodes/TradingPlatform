package brown.agent.pointprediction; 


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import brown.agent.library.SimpleAgent;
import brown.assets.value.FullType;
import brown.exceptions.AgentCreationException;
import brown.markets.SimpleAuction;
import brown.prediction.GoodPrice;
import brown.prediction.PointPrediction;
import brown.prediction.PredictionVector;
import brown.valuation.Valuation;
import brown.valuation.ValuationBundle;

public class TargetMVBidder extends SimpleAgent {
  
  private PointPrediction aPrediction;
  
  /**
   * constructor for auction 
   * @param host
   * @param port
   * @throws AgentCreationException
   */
  public TargetMVBidder(String host, int port,
      PointPrediction aPrediction) throws AgentCreationException {
    super(host, port);
    this.aPrediction = aPrediction; 
  }
  
  @Override
  public void onSimpleSealed(SimpleAuction market) {
    PredictionVector prediction = aPrediction.getPrediction();
    //map of the goods to be bid. 
    Map<FullType, Double> toBid = new HashMap<FullType, Double>();
    //acquisition bundle. 
    ValuationBundle acq = this.getAcquisition(prediction);
    //the acquisition should really only have one value, but iterate and
    //grab the first (since the utilities of all optimal bid bundles are
    //the same, we're indifferent.)
    for (Valuation types : acq) {
      //for every good in the prediction vector.
      for (GoodPrice p : aPrediction.getPrediction()) {
        if(types.contains(p.getGood())) {
          Double biddingPrice = calculateMarginalValue(p, prediction);
          toBid.put(p.getGood(), biddingPrice);
        }
        else {
          toBid.put(p.getGood(), 0.000001);
        }
      }
      //we only need one
      break;
      }
    //bid in the market. 
    System.out.println(toBid);
    market.bid(this, toBid);
  }
  
  /**
   * Helper function for acquiring the set of optimal bid bundles.
   * @param aPrediction
   * a price prediction vector- a good mapped to a projected going price in
   * the auction
   * @return
   * the aquisition, or the set of optimal bid bundles- packaged
   * in a valuation bundle, so they come with their valuations, 
   * for convenience of use.
   */
  private ValuationBundle getAcquisition(PredictionVector aPrediction) {
    //the return bundle
    ValuationBundle acquisition = new ValuationBundle();
    //max value to be determined
    Double maxValue = 0.0;
    //for each of the agent's private valuations run this block
    for (Valuation aVal : this.myValuation) {
      //get the price of the bundle
      Double value = aVal.getPrice();
      //for every good in the prediction vector run this block
      for(GoodPrice p : aPrediction) {
        //if the bundle contains this particular good, subtract the 
        //value of the good from value. Upon iteration this will give a 
        //utility for the given bundle.
        if(aVal.contains(p.getGood())) 
        value -= p.getPrice();
      }
      //determine the maximum utility. 
      maxValue = (value > maxValue) ? value : maxValue; 
    }
    //now that we have the max utility, for all bundles with this utility, 
    //add to the acquisition.
    for(Valuation types : this.myValuation) {
      if (types.getPrice() >= maxValue) {
        acquisition.add(types);
      }
    }
    //return
    return acquisition;
  }
  
  /**
   * Given a valuation, a point price prediction, and a good, 
   * calculates the marginal value of good. That is, the difference in 
   * utility between optimal bundles with and without the good.
   * @param optimal
   * @param aVec
   * @param good
   * @return
   */
  private Double calculateMarginalValue (GoodPrice good, PredictionVector aVec) {  
    //create a copy without the specified good
    PredictionVector goodFree = new PredictionVector(aVec);
    goodFree.add(good.getGood(), 0.0);
    PredictionVector goodUnavailable = new PredictionVector(aVec);
    goodUnavailable.add(good.getGood(), Double.POSITIVE_INFINITY);
    //get the acquisition bundle for copy
    ValuationBundle acqFree = this.getAcquisition(goodFree);
    ValuationBundle acqUnavailable = this.getAcquisition(goodUnavailable);

    
    Valuation optimalSansGood = new Valuation(null, null);
    for(Valuation v : acqUnavailable) {
      optimalSansGood = v;
      break;
    }
    Valuation optimalFreeGood = new Valuation(null, null);
    for(Valuation v : acqFree) {
      optimalFreeGood = v;
      break;
    }
    Double optimalVal = optimalFreeGood.getPrice();
    Double optimalVal2 = optimalSansGood.getPrice();
    for(GoodPrice g : aVec) {
      if (optimalFreeGood.contains(g.getGood())) {
        optimalVal -= g.getPrice();
      }
      if (optimalSansGood.contains(g.getGood())) {
        optimalVal2 -= g.getPrice();
      }
    }
    return (optimalVal - optimalVal2);
  }
  
  
  public static void main(String[] args) throws AgentCreationException {
    new TargetPriceBidder("localhost", 2121);
    while(true){}
  }
  
}