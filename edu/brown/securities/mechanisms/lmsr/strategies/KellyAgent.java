package brown.securities.mechanisms.lmsr.strategies;

import brown.agent.Agent;
import brown.assets.value.TradeableType;
import brown.auctions.wrappers.SimpleAuction;
import brown.exceptions.AgentCreationException;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.cda.ContinuousDoubleAuction;
import brown.securities.mechanisms.lmsr.LMSR;
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
	public void onSimpleSealed(SimpleAuction simpleWrapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSimpleOpenOutcry(SimpleAuction simpleWrapper) {
		// TODO Auto-generated method stub
		
	}
	
	protected double fractionalBet(double p_m, LMSR market) {
		if (p_m <= this.belief) {
			double fraction = (this.belief - p_m) / (1 - p_m);
			fraction *= this.money;
			return market.moniesToShares(fraction);
		} else {
			double fraction = (p_m - this.belief) / p_m;
			fraction *= this.money;
			return market.moniesToShares(fraction);
		}
	}

	@Override
	public void onLMSR(LMSR market) {
		double p_m = market.getTradeableType().TYPE == TradeableType.PredictionYes ? market.price() : 1-market.price();
		// = ???
		if (p_m <= this.belief) {
			if (market.getTradeableType().TYPE == TradeableType.PredictionYes) {
				market.buy(this, this.fractionalBet(p_m, market), 1);
			} else {
				market.sell(this, this.fractionalBet(p_m, market), 1);
			}
		} else if (p_m > this.belief){
			if (market.getTradeableType().TYPE == TradeableType.PredictionYes) {
				market.sell(this, this.fractionalBet(p_m, market), 1);
			} else {
				market.buy(this, this.fractionalBet(p_m, market), 1);
			}
		}
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
