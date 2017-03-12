package brown.lab.lab3;

import brown.agent.Agent;
import brown.exceptions.AgentCreationException;
import brown.lab.GameSetup;
import brown.lab.ValuationRegistration;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.auctions.BidReqeust;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.cda.CDAWrapper;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
import brown.setup.Logging;

public abstract class Lab3Agent extends Agent {

	private double myValuation;

	public Lab3Agent(String host, int port) throws AgentCreationException {
		super(host, port, new GameSetup());
	}

	@Override
	public void onRegistration(Registration registration) {
		super.onRegistration(registration);
		ValuationRegistration valuationRegistration = (ValuationRegistration) registration;
		this.myValuation = valuationRegistration.getValue();
		Logging.log("[+] max: " + this.myValuation);
	}

	@Override
	public void onAck(Ack message) {
		if (message.REJECTED) {
			Logging.log("[x] ack: " + message.failedBR);
		}
	}

	@Override
	public void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("[-] bank " + bankUpdate.newAccount.monies);
		if (bankUpdate.newAccount.tradeables.size() > 0) {
			Logging.log("[+] victory!");
		}
	}

	@Override
	public void onNegotiateRequest(NegotiateRequest tradeRequest) {
		// Noop
	}

	@Override
	public void onLMSR(LMSRWrapper market) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContinuousDoubleAuction(CDAWrapper market) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTradeRequest(BidReqeust bidRequest) {
		// TODO Auto-generated method stub

	}

}
