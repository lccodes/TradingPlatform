package brown.agent.library;

import brown.agent.Agent;
import brown.assets.value.TradeableType;
import brown.exceptions.AgentCreationException;
import brown.markets.ContinuousDoubleAuction;
import brown.markets.LMSR;
import brown.markets.SimpleAuction;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.messages.trades.NegotiateRequest;
import brown.registrations.PMRegistration;
import brown.setup.Logging;
import brown.setup.library.CDAGameSetup;
import brown.tradeables.Tradeable;

public class TestAgentCDA extends Agent {
	private int ME;
	public boolean myCoin;

	public TestAgentCDA(String host, int port) throws AgentCreationException {
		super(host, port, new CDAGameSetup());
		this.ME = 2;
		this.myCoin = false;
	}

	@Override
	public void onLMSR(LMSR market) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContinuousDoubleAuction(ContinuousDoubleAuction market) {
		Logging.log("[" + this.ID + "] "+ market.getAuctionID());
		if (this.ME == 0 && market.getTradeableType().TYPE.equals(TradeableType.PredictionYes)) {
			market.sell(this, 1, 10);
			this.ME = -1;
			Logging.log("[" + this.ID + "] sold yes");
		} else if (this.ME == 1 && market.getTradeableType().TYPE.equals(TradeableType.PredictionNo)){
			market.sell(this, 1, 10);
			Logging.log("[" + this.ID + "] sold no");
		} else if (this.ME == 2){
			market.buy(this, 1, 11);
			Logging.log("[" + this.ID + "] bought " + market.getTradeableType());
		}
	}

	@Override
	public void onAck(Ack message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTradeRequest(BidRequest bidRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNegotiateRequest(NegotiateRequest tradeRequest) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onBankUpdate(BankUpdate bankUpdate) {
		for (Tradeable t : bankUpdate.newAccount.tradeables) {
			if (t.getType().TYPE.equals(TradeableType.PredictionYes)) {
				this.ME = 0;
			} else {
				this.ME = 1;
			}
		}
		Logging.log("[" + this.ID + "] cash: "+ bankUpdate.newAccount.monies + " tradeables " + bankUpdate.newAccount.tradeables);
	}
	
	@Override
	public void onRegistration(Registration registration) {
		super.onRegistration(registration);
		PMRegistration reg = (PMRegistration) registration;
		this.myCoin = reg.COIN;
		Logging.log("[+] my coin: " + this.myCoin);
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
