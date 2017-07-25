package brown.agent.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import brown.assets.value.BasicType;
import brown.exceptions.AgentCreationException;
import brown.markets.SimpleAuction;
import brown.messages.markets.GameReport;
import brown.setup.Logging;

public class FinalProjectDemo extends FinalProjectAgent {

	public FinalProjectDemo(String host, int port) throws AgentCreationException {
		super(host, port);
	}

	@Override
	public void onSimpleSealed(SimpleAuction market) {
		System.out.println("Start");
		Map<BasicType, Double> toBid = new HashMap<BasicType,Double>();
		for (Entry<Set<BasicType>, Double> types : this.myValuation.entrySet()) {
			System.out.println("Sample others...");
			System.out.println(this.sampleValuation());
			for (BasicType type : types.getKey()) {
				toBid.put(type, types.getValue()/(double)types.getKey().size());
			}
			System.out.println("WORKING");
		}
		System.out.println("DONE");
		market.bid(this, toBid);
	}

	@Override
	public void onSimpleOpenOutcry(SimpleAuction market) {
		Set<BasicType> toBid = new HashSet<BasicType>();
		System.out.println(this.sampleValuation());
		for (Set<BasicType> types : this.myValuation.keySet()) {
			for (BasicType type : types) {
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
		new FinalProjectDemo("localhost", 2121);
		while(true){}
	}

}
