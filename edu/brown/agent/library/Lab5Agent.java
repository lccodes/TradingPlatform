package brown.agent.library;


import brown.exceptions.AgentCreationException;
import brown.markets.ContinuousDoubleAuction;
import brown.markets.LMSR;
import brown.markets.Lemonade;
import brown.messages.Ack;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.messages.markets.LemonadeReport;
import brown.messages.trades.NegotiateRequest;
import brown.setup.Logging;

public abstract class Lab5Agent extends LabAgent {

	public Lab5Agent(String host, int port) throws AgentCreationException {
		super(host, port);
	}

	@Override
	public void onSimpleSealed(SimpleOneSidedWrapper market) {
		this.onLemonade(new Lemonade(market));
	}

	@Override
	public void onSimpleOpenOutcry(SimpleOneSidedWrapper market) {
		Logging.log("[X] Error do not use outcry");
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
	public void onAck(Ack message) {
		if (message.REJECTED) {
			Logging.log("[x] failed: " + message.failedBR);
		}
	}

	@Override
	public void onMarketUpdate(GameReport marketUpdate) {
		this.onLemonadeUpdate((LemonadeReport) marketUpdate);
	}

	public abstract void onLemonadeUpdate(LemonadeReport lemonadeReport);

	@Override
	public void onTradeRequest(BidRequest bidRequest) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNegotiateRequest(NegotiateRequest tradeRequest) {
		// TODO Auto-generated method stub
	}

}
