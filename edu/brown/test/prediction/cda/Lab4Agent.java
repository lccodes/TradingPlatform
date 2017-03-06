package brown.test.prediction.cda;

import brown.agent.Agent;
import brown.assets.accounting.Account;
import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.Ack;
import brown.messages.auctions.BidReqeust;
import brown.messages.markets.TradeRequest;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
import brown.setup.Logging;

public abstract class Lab4Agent extends Agent {
	public boolean myCoin;
	public Account myBalance;
	
	public Lab4Agent(String host, int port)
			throws AgentCreationException {
		super(host, port, new GameSetup());
		this.myCoin = false;
		this.myBalance = new Account(null);
	}

	@Override
	public void onSimpleSealed(SimpleOneSidedWrapper market) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSimpleOpenOutcry(SimpleOneSidedWrapper market) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLMSR(LMSRWrapper market) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAck(Ack message) {
		Logging.log("Order rejected " + message.failedLO);
	}

	@Override
	public void onMarketUpdate(TradeRequest marketUpdate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("Current Bank: ");
		Logging.log("Monies = " + bankUpdate.newAccount.monies + " || Tradeables = " + bankUpdate.newAccount.tradeables);
		this.myBalance = bankUpdate.newAccount;
	}

	@Override
	public void onTradeRequest(BidReqeust bidRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNegotiateRequest(NegotiateRequest tradeRequest) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onRegistration(Registration registration) {
		super.onRegistration(registration);
		PMRegistration reg = (PMRegistration) registration;
		this.myCoin = reg.COIN;
		Logging.log("[+] my coin: " + this.myCoin);
	}

}
