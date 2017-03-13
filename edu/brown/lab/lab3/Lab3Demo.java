package brown.lab.lab3;

import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.markets.GameReport;
import brown.setup.Logging;

public class Lab3Demo extends Lab3Agent {

	public Lab3Demo(String host, int port) throws AgentCreationException {
		super(host, port);
	}

	@Override
	public void onSimpleSealed(SimpleOneSidedWrapper market) {
		double currentBid = market.getQuote();
		//Log the current bid
		Logging.log("Sealed Reserve: " + currentBid);
		//Random bid
		market.bid(this, Math.random() * 100);
	}

	@Override
	public void onSimpleOpenOutcry(SimpleOneSidedWrapper market) {
		double currentBid = market.getQuote();
		//Log the current bid
		Logging.log("Current quote: " + currentBid);
		//High bid + 1
		market.bid(this, currentBid+1);
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
