package brown.lab.lab4;

import brown.agent.Agent;
import brown.assets.accounting.Account;
import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.lab.UnitCDA;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.cda.ContinuousDoubleAuction;
import brown.securities.mechanisms.lmsr.LMSR;
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
	public void onContinuousDoubleAuction(ContinuousDoubleAuction market) {
		//Invoke the game specific subclass
		this.onContinuousDoubleAuction(new UnitCDA(market));
	}

	protected abstract void onContinuousDoubleAuction(UnitCDA unitCDAWrapper);

	@Override
	public void onSimpleSealed(SimpleOneSidedWrapper market) {
		// Noop
	}

	@Override
	public void onSimpleOpenOutcry(SimpleOneSidedWrapper market) {
		// Noop
	}

	@Override
	public void onLMSR(LMSR market) {
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
