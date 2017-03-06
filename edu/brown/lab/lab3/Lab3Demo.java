package brown.lab.lab3;

import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.markets.MarketUpdate;
import brown.setup.Logging;

public class Lab3Demo extends Lab3Agent {

	public Lab3Demo(String host, int port) throws AgentCreationException {
		super(host, port);
	}

	@Override
	public void onSimpleSealed(SimpleOneSidedWrapper market) {
		//Random bid
		market.bid(this, Math.random());
	}

	@Override
	public void onSimpleOpenOutcry(SimpleOneSidedWrapper market) {
		//Random bid + 1
		market.bid(this, Math.random()+1);
	}

	@Override
	public void onMarketUpdate(MarketUpdate marketUpdate) {
		Logging.log("New ledger!");
	}

}
