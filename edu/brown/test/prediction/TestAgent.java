package brown.test.prediction;

import brown.agent.Agent;
import brown.auctions.OneSidedWrapper;
import brown.auctions.TwoSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.Rejection;
import brown.messages.auctions.TradeRequest;
import brown.messages.markets.MarketUpdate;
import brown.messages.trades.NegotiateRequest;
import brown.setup.Logging;
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
		super(host, port, new GameSetup());
	}

	@Override
	protected void onSealedBid(OneSidedWrapper market) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void onOpenOutcry(OneSidedWrapper market) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void onLMSR(TwoSidedWrapper market) {
		Logging.log("cost " + market.quoteBid(1, 0));
		if (!this.done) {
			this.done = true;
			market.buy(this, 1, 0);
			Logging.log("bought 1");
		}
	}


	@Override
	protected void onContinuousDoubleAuction(TwoSidedWrapper market) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void onRejection(Rejection message) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void onMarketUpdate(MarketUpdate marketUpdate) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("cash: "+ bankUpdate.newAccount.monies);
	}


	@Override
	protected void onTradeRequest(TradeRequest bidRequest) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void onNegotiateRequest(NegotiateRequest tradeRequest) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new TestAgent("localhost", 9922);
		while(true) {}
	}

}