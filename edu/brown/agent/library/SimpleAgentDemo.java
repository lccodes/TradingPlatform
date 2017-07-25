package brown.agent.library; 

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import brown.assets.value.BasicType;
import brown.exceptions.AgentCreationException;
import brown.markets.SimpleAuction;
import brown.valuation.Valuation;

public class SimpleAgentDemo extends SimpleAgent {
  
  public SimpleAgentDemo(String host, int port) throws AgentCreationException {
    super(host, port);
  }

  @Override
  public void onSimpleSealed(SimpleAuction market) {
    Map<BasicType, Double> toBid = new HashMap<BasicType, Double>();
    for (Valuation types : this.myValuation) {
      for (BasicType type : types.getGoods()) {
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
  
  @Override
  	public void onSimpleOpenOutcry(SimpleAuction market) {
  		Map<BasicType, Double> toBid = new HashMap<BasicType, Double>();
  		for (Valuation types : this.myValuation) {
  			for (BasicType type : types.getGoods()) {
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
  
  public static void main(String[] args) throws AgentCreationException {
    new SimpleAgentDemo("caladan", 2122);
    while(true){}
  }
}