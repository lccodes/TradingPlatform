package brown.agent.pointprediction;

import java.util.HashMap;
import java.util.Map;

import brown.agent.library.SimpleAgent;
import brown.assets.value.FullType;
import brown.exceptions.AgentCreationException;
import brown.markets.SimpleAuction;
import brown.prediction.GoodPrice;
import brown.prediction.PointPrediction;
import brown.prediction.PredictionVector;
import brown.valuation.Valuation;
import brown.valuation.ValuationBundle;

/**
 * a point prediction utilizing agent, targetBidder
 * bids the goods in an optimal bundle (typically there is only one)
 * at the price given 
 * by the point prediction vector.
 * @author acoggins
 *
 */
public class TargetPriceBidder extends SimpleAgent {
  
  private PointPrediction aPrediction;
  /**
   * constructor for auction 
   * @param host
   * @param port
   * @throws AgentCreationException
   */
  public TargetPriceBidder(String host, int port) throws AgentCreationException {
    super(host, port);
  }
  
  /**
   * bids in simple sealed auction. 
   * @param market
   * the market to be bid in.
   */
  public void onSimpleOpenOutcry(SimpleAuction market) {
    aPrediction = new PointPrediction();
    for(FullType f : this.allGoods) {
      GoodPrice good = new GoodPrice(f, 3.0);
      aPrediction.setPrediction(good);
    }
    //map of the goods to be bid. 
    Map<FullType, Double> toBid = new HashMap<FullType, Double>();
    //acquisition bundle. 
    ValuationBundle acq = this.getAcquisition(aPrediction.getPrediction());
    //the acquisition should really only have one value, but iterate and
    //grab the first (since the utilities of all optimal bid bundles are
    //the same, we're indifferent.)
    for (Valuation types : acq) {
      //for every good in the prediction vector.
      for (GoodPrice p : aPrediction.getPrediction()) {
        //if the good is in the optimal bundle, add it to toBid
        if(types.contains(p.getGood())) {
          toBid.put(p.getGood(), p.getPrice());
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
    ValuationBundle copy = new ValuationBundle(this.myValuation);
    ValuationBundle acquisition = new ValuationBundle();
    //max value to be determined
    Double maxValue = 0.0;
    //for each of the agent's private valuations run this block
    for (Valuation aVal : copy) {
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
      copy.add(aVal.getGoods(), value);
      //determine the maximum utility. 
      maxValue = (value > maxValue) ? value : maxValue; 
    }
    //now that we have the max utility, for all bundles with this utility, 
    //add to the acquisition.
    for(Valuation types : copy) {
      if (types.getPrice() >= maxValue) {
        acquisition.add(types);
      }
    }
    //return
    System.out.println("acquisition " + acquisition);
    return acquisition;
  }
  
  public static void main(String[] args) throws AgentCreationException {
    new TargetPriceBidder("caladan", 2122);
    while(true){}
  }
  
}