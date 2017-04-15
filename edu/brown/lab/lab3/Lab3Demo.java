package brown.lab.lab3;

import java.util.HashMap;
import java.util.Map;

import brown.assets.value.FullType;
import brown.auctions.wrappers.SimpleWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.markets.GameReport;
import brown.setup.Logging;

public class Lab3Demo extends Lab3Agent {

	public Lab3Demo(String host, int port) throws AgentCreationException {
		super(host, port);
	}

	@Override
	public void onSimpleSealed(SimpleWrapper market) {
		market.bid(this, this.myValuation);
	}

	@Override
	public void onSimpleOpenOutcry(SimpleWrapper market) {
		Map<FullType, Double> toBid = new HashMap<FullType, Double>();
		for (FullType type : this.myValuation.keySet()) {
			if (this.myValuation.containsKey(type) && market.getHighBid(type).PRICE < this.myValuation.get(type)) {
				System.out.println("BID: " + market.getHighBid(type));
				toBid.put(type, this.myValuation.get(type));
			}
		}
		
		market.bid(this, toBid);
	}

	@Override
	public void onMarketUpdate(GameReport marketUpdate) {
		Logging.log("Market outcome:");
		//TODO: Log market outcome
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new Lab3Demo("localhost", 2121);
		while(true){}
	}

}
