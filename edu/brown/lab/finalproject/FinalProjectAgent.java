package brown.lab.finalproject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
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
import brown.securities.mechanisms.cda.ContinuousDoubleAuction;
import brown.securities.mechanisms.lmsr.LMSR;
import brown.setup.Logging;

public abstract class FinalProjectAgent extends Agent {
	protected Map<Set<FullType>, Double> myValuation;
	private Map<Set<FullType>, Double> toSample;

	public FinalProjectAgent(String host, int port) throws AgentCreationException {
		super(host, port, new GameSetup());
		this.myValuation = new HashMap<Set<FullType>, Double>();
		this.toSample = new HashMap<Set<FullType>, Double>();
	}

	@Override
	public void onRegistration(Registration registration) {
		super.onRegistration(registration);
		ValuationRegistration valuationRegistration = (ValuationRegistration) registration;
		this.myValuation.putAll(valuationRegistration.getValues());
		Logging.log("[+] new XOR values: " + valuationRegistration.getValues());
		for (Entry<Set<FullType>, Double> entry : this.myValuation.entrySet()) {
			Set<FullType> theSet = new HashSet<FullType>();
			for (FullType t : entry.getKey()) {
				if (theSet.size() == 0 || Math.random() > .33) {
					theSet.add(t);
				}
			}
			double rando = Math.random() * entry.getValue() * .15;
			rando = Math.random() > .5 ? rando : rando*-1;
			this.toSample.put(theSet, entry.getValue()+rando);
		}
		System.out.println("[+] registered as " + this.ID);
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
	
	public Map<Set<FullType>, Double> sampleValuation() {
		resample();
		return this.toSample;
	}
	
	public Map<Set<FullType>, Double> sampleValuation(int agentNum) {
		resample();
		return this.toSample;
	}
	
	public void resample() {
		this.toSample.clear();
		for (Entry<Set<FullType>, Double> entry : this.myValuation.entrySet()) {
			Set<FullType> theSet = new HashSet<FullType>();
			for (FullType t : entry.getKey()) {
				//if (theSet.size() == 0 || Math.random() > .33) {
					theSet.add(t);
				//}
			}
			double rando = Math.random() * entry.getValue() * .15;
			rando = Math.random() > .5 ? rando : rando*-1;
			this.toSample.put(theSet, entry.getValue()+rando);
		}
	}

}
