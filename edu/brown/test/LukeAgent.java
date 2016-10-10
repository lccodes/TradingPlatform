package brown.test;

import brown.agent.Agent;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.BidRequest;
import brown.messages.MarketUpdate;
import brown.messages.TradeRequest;
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
		super(host, port);
		GameSetup.setup(this.CLIENT.getKryo());
	}

	@Override
	protected void onBankUpdate(BankUpdate bankUpdate) {
		System.out.println(bankUpdate.getID().equals(this.ID));
	}

	@Override
	protected void onBidRequest(BidRequest bidRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTradeRequest(TradeRequest tradeRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMarketUpdate(MarketUpdate marketUpdate) {
		for(SecurityWrapper pm : marketUpdate.MARKETS) {
			if (pm.getPriceYes(1) < .85) {
				pm.buyYes(this, 1);
			} else {
				System.out.println(pm.getPriceYes(1));
			}
		}
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new LukeAgent("localhost", 9922);
		while(true) {}
	}

}