package brown.test;

import brown.agent.Agent;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.BidRequest;
import brown.messages.TradeRequest;

public class TestAgent extends Agent {

	public TestAgent(String host, int port) throws AgentCreationException {
		super(host, port);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onBankUpdate(BankUpdate bankUpdate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onBidRequest(BidRequest bidRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTradeRequest(TradeRequest tradeRequest) {
		// TODO Auto-generated method stub
		
	}

}
