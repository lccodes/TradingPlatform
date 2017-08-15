package brown.agent.library;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import brown.agent.Agent;
import brown.assets.value.BasicType;
import brown.exceptions.AgentCreationException;
import brown.markets.ContinuousDoubleAuction;
import brown.markets.LMSR;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.auctions.BidRequest;
import brown.messages.trades.NegotiateRequest;
import brown.registrations.ValuationRegistration;
import brown.setup.Logging;
import brown.setup.library.LabGameSetup;

public abstract class Lab8Agent extends Agent {
	protected Map<Set<BasicType>, Double> myValuation;

	public Lab8Agent(String host, int port) throws AgentCreationException {
		super(host, port, new LabGameSetup());
		this.myValuation = new HashMap<Set<BasicType>, Double>();
	}

	@Override
	public void onRegistration(Registration registration) {
		super.onRegistration(registration);
		ValuationRegistration valuationRegistration = (ValuationRegistration) registration;
		this.myValuation.putAll(valuationRegistration.getValues());
		Logging.log("[+] new XOR values: " + valuationRegistration.getValues());
	}

	@Override
	public void onAck(Ack message) {
		if (message.REJECTED) {
			Logging.log("[x] rej: " + message.failedBR);
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
	public void onLMSR(LMSR market) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContinuousDoubleAuction(ContinuousDoubleAuction market) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTradeRequest(BidRequest bidRequest) {
		// TODO Auto-generated method stub

	}

}
