package brown.test.prediction;

import brown.agent.Agent;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.Rejection;
import brown.messages.auctions.TradeRequest;
import brown.messages.markets.MarketUpdate;
import brown.messages.trades.NegotiateRequest;
import brown.securities.SecurityWrapper;
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
public class LukeAgent extends Agent {

	public LukeAgent(String host, int port) throws AgentCreationException {
		super(host, port, new GameSetup());
	}

	@Override
	protected void onBankUpdate(BankUpdate bankUpdate) {
		System.out.println(bankUpdate.getID().equals(this.ID));
	}

	@Override
	protected void onTradeRequest(TradeRequest bidRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onNegotiateRequest(NegotiateRequest tradeRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMarketUpdate(MarketUpdate marketUpdate) {
		for(SecurityWrapper pm : marketUpdate.MARKETS) {
			if (pm.bid(1) < .85) {
				pm.buy(this, 1);
			} else {
				System.out.println(pm.bid(1));
			}
		}
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new LukeAgent("localhost", 9922);
		while(true) {}
	}

	@Override
	protected void onRejection(Rejection message) {
		// TODO Auto-generated method stub
		
	}

}