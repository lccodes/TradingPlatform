package brown.test.prediction.lmsr;

import brown.agent.Agent;
import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.Rejection;
import brown.messages.auctions.BidReqeust;
import brown.messages.markets.TradeRequest;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.cda.CDAWrapper;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
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
	protected void onLMSR(LMSRWrapper market) {
		Logging.log("cost " + market.quoteBid(1));
		if (!this.done && Math.random() < .25) {
			this.done = true;
			market.buy(this, 1, .6);
			Logging.log("bought 1 " + market.getType().TYPE);
		}
	}


	@Override
	protected void onContinuousDoubleAuction(CDAWrapper market) {
		// TODO Auto-generated method stub
	}


	@Override
	protected void onRejection(Rejection message) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void onMarketUpdate(TradeRequest marketUpdate) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("cash: "+ bankUpdate.newAccount.monies + " tradeables " + bankUpdate.newAccount.goods);
	}


	@Override
	protected void onTradeRequest(BidReqeust bidRequest) {
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

	@Override
	protected void onSimpleSealed(SimpleOneSidedWrapper market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onSimpleOpenOutcry(SimpleOneSidedWrapper market) {
		// TODO Auto-generated method stub
		
	}

}