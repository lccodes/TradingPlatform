package brown.lab.lab3;

import java.util.HashSet;
import java.util.Set;

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
		Set<FullType> toBid = new HashSet<FullType>();
		for (FullType type : this.myValuation.keySet()) {
			if (this.myValuation.containsKey(type) && market.getHighBid(type).PRICE < this.myValuation.get(type)) {
				toBid.add(type);
			}
		}
		
		if (toBid.size() != 0) {
			market.demandBid(this, toBid);
		}
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
