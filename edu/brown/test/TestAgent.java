package brown.test;

import brown.agent.Agent;
import brown.assets.Account;
import brown.exceptions.AgentCreationException;
import brown.markets.PredictionMarket;
import brown.messages.BankUpdate;
import brown.messages.BidRequest;
import brown.messages.MarketUpdate;
import brown.messages.TradeRequest;

public class TestAgent extends Agent {
	private boolean first = true;

	public TestAgent(String host, int port) throws AgentCreationException {
		super(host, port);
		GameSetup.setup(this.CLIENT.getKryo());
	}

	@Override
	protected void onBankUpdate(BankUpdate bankUpdate) {
		System.out.println(bankUpdate.getID().equals(this.ID));
		Account account = bankUpdate.newAccount;
		System.out.println(account.monies == 100.0 || account.monies <= 98.6);
		if (account.monies == 100.0) {
			System.out.println(account.shares.size() == 0);
		} else {
			System.out.println(account.shares.size() == 1);
		}
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
		for(PredictionMarket pm : marketUpdate.MARKETS) {
			if (first) {
				System.out.println(pm.getPriceYes(2) <= 1.44);
				pm.buyYes(this, 2);
				first = false;
			} else {
				System.out.println(pm.getPriceYes(2) >= 1.7);
			}
		}
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new TestAgent("localhost", 9922);
		while(true) {}
	}

}
