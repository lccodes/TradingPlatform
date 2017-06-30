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

public class Lab8Demo extends Lab8Agent {

	public Lab8Demo(String host, int port) throws AgentCreationException {
		super(host, port);
	}

	@Override
	public void onSimpleSealed(SimpleAuction market) {
		System.out.println("Start");
		Map<FullType, Double> toBid = new HashMap<FullType,Double>();
		for (Entry<Set<FullType>, Double> types : this.myValuation.entrySet()) {
			for (FullType type : types.getKey()) {
				//bidding logic 
				toBid.put(type, types.getValue()/(double)types.getKey().size());
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
		for (Set<FullType> types : this.myValuation.keySet()) {
			for (FullType type : types) {
				if (market.getMarketState(type).PRICE < Math.min(100,this.myValuation.get(types))) {
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
