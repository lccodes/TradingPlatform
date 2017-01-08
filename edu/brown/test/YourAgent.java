package brown.test;

import brown.agent.Agent;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.Rejection;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.MarketUpdate;
import brown.messages.trades.TradeRequest;
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
public class YourAgent extends Agent {

	public YourAgent(String host, int port) throws AgentCreationException {
		super(host, port);
		GameSetup.setup(this.CLIENT.getKryo());
	}

	@Override
	protected void onMarketUpdate(MarketUpdate marketUpdate) {
		// TODO
		/* This method gets invoked whenever any of the prediction markets
		 * change state. You will get an update when you purchase shares or
		 * when another agent purchases shares. 
		 */
	}

	@Override
	protected void onBankUpdate(BankUpdate bankUpdate) {
		// TODO Auto-generated method stub
		/* This method gets invoked whenever your bank account
		 * changes state. Either positive or negative. 
		 */
	}

	@Override
	protected void onBidRequest(BidRequest bidRequest) {
		// IGNORE FOR NOW
	}

	@Override
	protected void onTradeRequest(TradeRequest tradeRequest) {
		// IGNORE FOR NOW
	}
	
	public void myLogc() {
		//TODO
		//I'm run inside of a while loop. You can write your agent
		//logic however you please but this is a decent structure.
	}

	public static void main(String[] args) throws AgentCreationException {
		// If you are running the server on the same computer as your agent
		// then use localhost otherwise use the name or ip of the computer
		// hosting the server
		// 9922 is a good port but use any port > 1000 but MAKE SURE IT IS
		// WHAT YOUR SERVER IS USING
		YourAgent agent = new YourAgent("localhost", 9922);
		while(true) {
			agent.myLogc();
		}
	}

	@Override
	protected void onRejection(Rejection message) {
		// TODO Auto-generated method stub
		
	}

}
