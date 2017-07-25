package brown.agent.dummy;
import java.util.HashMap;
import java.util.Map;

import brown.agent.library.SimpleAgent;
import brown.agent.library.SimpleAgentDemo;
import brown.assets.value.FullType;
import brown.exceptions.AgentCreationException;
import brown.markets.SimpleAuction;
import brown.valuation.Valuation;


/**
 * simple agent that bids on set of goods at average valuation price. 
 * mainly for debugging.
 * @author acoggins
 *
 */
public class DummyAgent extends SimpleAgent {
  
  /**
   * constructor
   * @param host
   * server host
   * @param port
   * server port
   * @throws AgentCreationException
   */
  public DummyAgent(String host, int port) throws AgentCreationException {
    super(host, port);
  }

  @Override
  public void onSimpleSealed(SimpleAuction market) {
    Map<FullType, Double> toBid = new HashMap<FullType, Double>();
    for (Valuation types : this.myValuation) {
      for (FullType type : types.getGoods()) {
        if(types.getPrice()  > 0) {
          if(!toBid.containsKey(type)) {
          toBid.put(type, types.getPrice() / types.size());
        }
          else {
            if(toBid.get(type) < types.getPrice()) {
              toBid.put(type, types.getPrice() / types.size());
            }
          }
        }
        else {
          toBid.put(type, 0.1);
        }
      }
    }
    
    if (toBid.size() != 0) {
      System.out.println(toBid);
      market.bid(this, toBid);
    }
  }
  
  //this one is used.
  @Override
    public void onSimpleOpenOutcry(SimpleAuction market) {
      Map<FullType, Double> toBid = new HashMap<FullType, Double>();
      for (Valuation types : this.myValuation) {
        for (FullType type : types.getGoods()) {
          if(types.getPrice()  > 0) {
            toBid.put(type, types.getPrice());
          }
          else {
            toBid.put(type, 0.1);
          }
        }
      }
      
      if (toBid.size() != 0) {
        market.bid(this, toBid);
      }
    }
  
  /**
   * main class, for running
   * @param args
   * @throws AgentCreationException
   */
  public static void main(String[] args) throws AgentCreationException {
    new DummyAgent("caladan", 2122);
    while(true){}
  }
}