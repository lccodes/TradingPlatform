package brown.test.prediction;

import brown.agent.Agent;
import brown.assets.accounting.Account;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.Rejection;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.MarketUpdate;
import brown.messages.trades.TradeRequest;
import brown.securities.SecurityWrapper;

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
			System.out.println(account.goods.size() == 0);
		} else {
			System.out.println(account.goods.size() == 1);
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
		for(SecurityWrapper pm : marketUpdate.MARKETS) {
			if (first) {
				System.out.println(pm.bid(2) <= 1.44);
				pm.buy(this, 2);
				first = false;
			} else {
				System.out.println(pm.bid(2) >= 1.7);
			}
		}
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new TestAgent("localhost", 9922);
		while(true) {}
	}

	@Override
	protected void onRejection(Rejection message) {
		// TODO Auto-generated method stub
		
	}

}
