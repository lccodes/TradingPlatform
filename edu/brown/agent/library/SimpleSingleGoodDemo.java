package brown.agent.library; 

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import brown.assets.value.FullType;
import brown.exceptions.AgentCreationException;
import brown.markets.SimpleAuction;
import brown.valuation.Valuation;

public class SimpleSingleGoodDemo extends SimpleSingleGoodAgent {
  
  public SimpleSingleGoodDemo(String host, int port) throws AgentCreationException {
    super(host, port);
  }

  @Override
  public void onSimpleSealed(SimpleAuction market) {
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
      System.out.println(toBid);
      market.bid(this, toBid);
    }
  }
  
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
  
  public static void main(String[] args) throws AgentCreationException {
    new SimpleSingleGoodDemo("caladan", 2122);
    while(true){}
  }
}