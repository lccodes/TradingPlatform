package brown.agent.library;

import brown.agent.Agent;
import brown.exceptions.AgentCreationException;
import brown.markets.ContinuousDoubleAuction;
import brown.markets.LMSR;
import brown.markets.SimpleAuction;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.messages.trades.NegotiateRequest;
import brown.setup.Logging;
import brown.setup.library.LMSRGameSetup;
/**
 * 
 * @author <YOUR NAME>
 * Implement this agent to bid in the single fake prediction market
 * produced by the TestServer. Test your agent by running it on the test
 * server. To launch a test server, run LaunchTestGame as a java application.
 * You can bid in the prediction market by running .buyYes or .buyNo on the
 * prediction market passed to you by the MarketUpdate. This method will
 * automatically update the server.
 *
 */
public class TestAgent extends Agent {
	private boolean done = false;

	public TestAgent(String host, int port) throws AgentCreationException {
		super(host, port, new LMSRGameSetup());
	}

	@Override
	public void onLMSR(LMSR market) {
		Logging.log("cost " + market.quoteBid(1));
		if (!this.done && Math.random() < .25) {
			this.done = true;
			market.buy(this, 1, .6);
			Logging.log("bought 1 " + market.getTradeableType());
		}
	}


	@Override
	public void onContinuousDoubleAuction(ContinuousDoubleAuction market) {
		// TODO Auto-generated method stub
	}


	@Override
	public void onAck(Ack message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("cash: "+ bankUpdate.newAccount.monies + " tradeables " + bankUpdate.newAccount.tradeables);
	}


	@Override
	public void onTradeRequest(BidRequest bidRequest) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onNegotiateRequest(NegotiateRequest tradeRequest) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new TestAgent("localhost", 9922);
		while(true) {}
	}

	@Override
	public void onMarketUpdate(GameReport marketUpdate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSimpleSealed(SimpleAuction simpleWrapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSimpleOpenOutcry(SimpleAuction simpleWrapper) {
		// TODO Auto-generated method stub
		
	}

}