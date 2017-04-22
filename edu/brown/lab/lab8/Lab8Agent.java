package brown.lab.lab8;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import brown.agent.Agent;
import brown.assets.value.FullType;
import brown.exceptions.AgentCreationException;
import brown.lab.GameSetup;
import brown.lab.ValuationRegistration;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.auctions.BidRequest;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.cda.CDAWrapper;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
import brown.setup.Logging;

public abstract class Lab8Agent extends Agent {
	protected Map<Set<FullType>, Double> myValuation;

	public Lab8Agent(String host, int port) throws AgentCreationException {
		super(host, port, new GameSetup());
		this.myValuation = new HashMap<Set<FullType>, Double>();
	}

	@Override
	public void onRegistration(Registration registration) {
		super.onRegistration(registration);
		ValuationRegistration valuationRegistration = (ValuationRegistration) registration;
		this.myValuation.putAll(valuationRegistration.getValues());
		Logging.log("[+] new XOR bids: " + this.myValuation);
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
	public void onLMSR(LMSRWrapper market) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContinuousDoubleAuction(CDAWrapper market) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTradeRequest(BidRequest bidRequest) {
		// TODO Auto-generated method stub

	}

}
