package brown.test.prediction.cda;

import brown.agent.Agent;
import brown.assets.value.SecurityType;
import brown.assets.value.Tradeable;
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

public class TestAgent extends Agent {
	private int ME;

	public TestAgent(String host, int port) throws AgentCreationException {
		super(host, port, new GameSetup());
		this.ME = 2;
	}

	@Override
	protected void onLMSR(LMSRWrapper market) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onContinuousDoubleAuction(CDAWrapper market) {
		Logging.log("[" + this.ID + "] "+ market.getID());
		if (this.ME == 0 && market.getType().TYPE.equals(SecurityType.PredictionYes)) {
			market.sell(this, 1, 10);
			this.ME = -1;
			Logging.log("[" + this.ID + "] sold yes");
		} else if (this.ME == 1 && market.getType().TYPE.equals(SecurityType.PredictionNo)){
			market.sell(this, 1, 10);
			Logging.log("[" + this.ID + "] sold no");
		} else if (this.ME == 2){
			market.buy(this, 1, 11);
			Logging.log("[" + this.ID + "] bought " + market.getType().TYPE);
		}
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
	protected void onTradeRequest(BidReqeust bidRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNegotiateRequest(NegotiateRequest tradeRequest) {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onBankUpdate(BankUpdate bankUpdate) {
		for (Tradeable t : bankUpdate.newAccount.goods) {
			if (t.getType().TYPE.equals(SecurityType.PredictionYes)) {
				this.ME = 0;
			} else {
				this.ME = 1;
			}
		}
		Logging.log("[" + this.ID + "] cash: "+ bankUpdate.newAccount.monies + " tradeables " + bankUpdate.newAccount.goods);
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
