package brown.lab.lab5;

import brown.agent.Agent;
import brown.assets.accounting.Account;
import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.auctions.BidReqeust;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
import brown.setup.Logging;
import brown.test.prediction.cda.GameSetup;
import brown.test.prediction.cda.PMRegistration;

public abstract class Lab5Agent extends Agent {
	public boolean myCoin;
	public int myNumDecoys;
	public Account myBalance;
	
	public Lab5Agent(String host, int port)
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
		this.myNumDecoys = reg.DECOYCOUNT;
		Logging.log("[+] my coin: " + this.myCoin);
	}

}
