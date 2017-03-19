package brown.securities.mechanisms.lmsr.strategies;

import brown.agent.Agent;
import brown.assets.value.TradeableType;
import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.cda.CDAWrapper;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
import brown.setup.Setup;

public abstract class KellyAgent extends Agent {
	protected double money;
	protected double belief;

	public KellyAgent(String host, int port, Setup gameSetup)
			throws AgentCreationException {
		super(host, port, gameSetup);
		this.money = 0;
		this.belief = 0;
	}

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
		double p_m = market.getTradeableType().TYPE == TradeableType.PredictionYes ? market.price() : 1-market.price();
		if (p_m <= this.belief) {
			double fraction = (this.belief - p_m) / (1 - p_m);
			fraction *= this.money;
			if (market.getTradeableType().TYPE == TradeableType.PredictionYes) {
				market.buy(this, market.moniesToShares(fraction), 1);
			} else {
				market.sell(this, market.moniesToShares(fraction), 1);
			}
		} else {
			double fraction = (p_m - this.belief) / p_m;
			fraction *= this.money;
			if (market.getTradeableType().TYPE == TradeableType.PredictionYes) {
				market.sell(this, market.moniesToShares(fraction), 1);
			} else {
				market.buy(this, market.moniesToShares(fraction), 1);
			}
		}
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
		this.money = bankUpdate.newAccount.monies;
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
