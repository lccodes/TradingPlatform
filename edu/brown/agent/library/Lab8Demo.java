package brown.agent.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import brown.assets.value.FullType;
import brown.exceptions.AgentCreationException;
import brown.markets.SimpleAuction;
import brown.messages.markets.GameReport;
import brown.setup.Logging;
import brown.valuation.Valuation;

public class Lab8Demo extends Lab8Agent {

	public Lab8Demo(String host, int port) throws AgentCreationException {
		super(host, port);
	}

	@Override
	public void onSimpleSealed(SimpleAuction market) {
		System.out.println("Start");
		Map<FullType, Double> toBid = new HashMap<FullType,Double>();
		for (Valuation types : this.myValuation) {
			for (FullType type : types.getGoods()) {
				//bidding logic 
				toBid.put(type, types.getPrice()/(double)types.size());
			}
			System.out.println("WORKING");
		}
		System.out.println(toBid);
		System.out.println("DONE");
		market.bid(this, toBid);
	}

	@Override
	public void onSimpleOpenOutcry(SimpleAuction market) {
		Set<FullType> toBid = new HashSet<FullType>();
		for (Valuation types : this.myValuation) {
			for (FullType type : types.getGoods()) {
				if (market.getMarketState(type).PRICE < Math.min(100,types.getPrice())) {
					toBid.add(type);
				}
			}
		}
		
		if (toBid.size() != 0) {
			market.demandSet(this, toBid);
		}
	}

	@Override
	public void onMarketUpdate(GameReport marketUpdate) {
		Logging.log("Market outcome:");
		//TODO: Log market outcome
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new Lab8Demo("caladan", 2121);
		while(true){}
	}

}
