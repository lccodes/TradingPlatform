package brown.lab.lab4;

import brown.agent.Agent;
import brown.assets.accounting.Account;
import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.lab.UnitCDAWrapper;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.cda.CDAWrapper;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
import brown.setup.Logging;
import brown.test.prediction.cda.GameSetup;
import brown.test.prediction.cda.PMRegistration;

public abstract class Lab4Agent extends Agent {
	public boolean myCoin;
	public int myNumDecoys;
	public Account myBalance;
	
	public Lab4Agent(String host, int port)
			throws AgentCreationException {
		super(host, port, new GameSetup());
		this.myCoin = false;
		this.myBalance = new Account(null);
	}
	
	@Override
	public void onContinuousDoubleAuction(CDAWrapper market) {
		//Invoke the game specific subclass
		this.onContinuousDoubleAuction(new UnitCDAWrapper(market));
	}

	protected abstract void onContinuousDoubleAuction(UnitCDAWrapper unitCDAWrapper);

	@Override
	public void onSimpleSealed(SimpleOneSidedWrapper market) {
		// Noop
	}

	@Override
	public void onSimpleOpenOutcry(SimpleOneSidedWrapper market) {
		// Noop
	}

	@Override
	public void onLMSR(LMSRWrapper market) {
		// Noop
	}

	@Override
	public void onAck(Ack message) {
		if (message.REJECTED) {
			Logging.log("Order rejected " + message.failedLO);
		}
	}

	@Override
	public void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("Current Bank: ");
		Logging.log("Monies = " + bankUpdate.newAccount.monies + " || Tradeables = " + bankUpdate.newAccount.tradeables);
		this.myBalance = bankUpdate.newAccount;
	}

	@Override
	public void onTradeRequest(BidRequest bidRequest) {
		// Noop
	}

	@Override
	public void onNegotiateRequest(NegotiateRequest tradeRequest) {
		// Noop
	}
	
	@Override
	public void onRegistration(Registration registration) {
		super.onRegistration(registration);
		PMRegistration reg = (PMRegistration) registration;
		this.myCoin = reg.COIN;
		this.myNumDecoys = reg.DECOYCOUNT;
		Logging.log("[+] my coin: " + this.myCoin);
	}
	
	@Override
	public void onMarketUpdate(GameReport report) {
		// Noop
	}

}
