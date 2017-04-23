package brown.securities.mechanisms.lmsr.strategies;

import brown.agent.Agent;
import brown.auctions.wrappers.SimpleWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.cda.CDAWrapper;
import brown.setup.Setup;

public abstract class ExperimentalAgent extends Agent {
	protected double SIGNAL;
	protected double BUDGET;
	protected final int TIME;
	protected int CURRENTTIME;

	public ExperimentalAgent(String host, int port, Setup gameSetup, 
			double signal, int timeToGo) throws AgentCreationException {
		super(host, port, gameSetup);
		this.SIGNAL = signal;
		this.BUDGET = 0;
		this.TIME = timeToGo;
		this.CURRENTTIME = 0;
	}

	@Override
	public void onSimpleSealed(SimpleWrapper simpleWrapper) {
		// Noop
	}

	@Override
	public void onSimpleOpenOutcry(SimpleWrapper simpleWrapper) {
		// Noop
	}

	@Override
	public void onContinuousDoubleAuction(CDAWrapper market) {
		// Noop
	}

	@Override
	public void onAck(Ack message) {
		// Noop
	}

	@Override
	public void onMarketUpdate(GameReport marketUpdate) {
		// Noop
	}

	@Override
	public void onBankUpdate(BankUpdate bankUpdate) {
		this.BUDGET = bankUpdate.newAccount.monies;
	}

	@Override
	public void onTradeRequest(BidRequest bidRequest) {
		// Noop
	}

	@Override
	public void onNegotiateRequest(NegotiateRequest tradeRequest) {
		// Noop
	}

}
