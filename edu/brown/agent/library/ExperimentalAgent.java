package brown.agent.library;

import brown.agent.Agent;
import brown.exceptions.AgentCreationException;
import brown.markets.ContinuousDoubleAuction;
import brown.markets.SimpleAuction;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.messages.trades.NegotiateRequest;
import brown.setup.Setup;

public abstract class ExperimentalAgent extends Agent {
	protected double SIGNAL;
	protected double BUDGET;
	protected int TIME;
	protected int CURRENTTIME;

	public ExperimentalAgent(String host, int port, Setup gameSetup, 
			double signal) throws AgentCreationException {
		super(host, port, gameSetup);
		this.SIGNAL = signal;
		this.BUDGET = 0;
		this.TIME = -1;
		this.CURRENTTIME = 0;
	}

	@Override
	public void onSimpleSealed(SimpleAuction simpleWrapper) {
		// Noop
	}

	@Override
	public void onSimpleOpenOutcry(SimpleAuction simpleWrapper) {
		// Noop
	}

	@Override
	public void onContinuousDoubleAuction(ContinuousDoubleAuction market) {
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
	
	public void setTime(int time) {
		this.TIME = time;
		this.CURRENTTIME = 0;
	}

}
