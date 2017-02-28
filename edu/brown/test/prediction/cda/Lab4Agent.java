package brown.test.prediction.cda;

import brown.agent.Agent;
import brown.assets.accounting.Account;
import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.Rejection;
import brown.messages.auctions.BidReqeust;
import brown.messages.markets.TradeRequest;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
import brown.setup.Logging;

public abstract class Lab4Agent extends Agent {
	protected boolean myCoin;
	protected Account myBalance;
	
	public Lab4Agent(String host, int port)
			throws AgentCreationException {
		super(host, port, new GameSetup());
		this.myCoin = false;
		this.myBalance = new Account(null);
	}

	@Override
	protected void onSimpleSealed(SimpleOneSidedWrapper market) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSimpleOpenOutcry(SimpleOneSidedWrapper market) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onLMSR(LMSRWrapper market) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onRejection(Rejection message) {
		Logging.log("Order rejected " + message.failedLO);
	}

	@Override
	protected void onMarketUpdate(TradeRequest marketUpdate) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("Current Bank: ");
		Logging.log("Monies = " + bankUpdate.newAccount.monies + " || Tradeables = " + bankUpdate.newAccount.goods);
		this.myBalance = bankUpdate.newAccount;
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
	protected void onRegistration(Registration registration) {
		super.onRegistration(registration);
		PMRegistration reg = (PMRegistration) registration;
		this.myCoin = reg.COIN;
		Logging.log("[+] my coin: " + this.myCoin);
	}

}
