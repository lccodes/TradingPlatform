package brown.test;

import brown.agent.Agent;
import brown.assets.Account;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.BidRequest;
import brown.messages.TradeRequest;

public class TestAgent extends Agent {

	public TestAgent(String host, int port) throws AgentCreationException {
		super(host, port);
		GameSetup.setup(this.CLIENT.getKryo());
	}

	@Override
	protected void onBankUpdate(BankUpdate bankUpdate) {
		System.out.println(bankUpdate.getID().equals(this.ID));
		Account account = bankUpdate.newAccount;
		System.out.println(account.monies == 100.0);
	}

	@Override
	protected void onBidRequest(BidRequest bidRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTradeRequest(TradeRequest tradeRequest) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new TestAgent("localhost", 9922);
		while(true) {}
	}

}
