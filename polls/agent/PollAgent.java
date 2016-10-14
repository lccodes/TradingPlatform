package agent;

import brown.agent.Agent;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.BidRequest;
import brown.messages.MarketUpdate;
import brown.messages.Rejection;
import brown.messages.TradeRequest;
import brown.test.GameSetup;

public abstract class PollAgent extends Agent {

  public PollAgent(String host, int port) throws AgentCreationException {
    super(host, port);
    GameSetup.setup(this.CLIENT.getKryo());
  }

  @Override
  protected abstract void onRejection(Rejection message);

  @Override
  protected abstract void onMarketUpdate(MarketUpdate marketUpdate);

  @Override
  protected abstract void onBankUpdate(BankUpdate bankUpdate);

  @Override
  protected void onBidRequest(BidRequest bidRequest) {
    // TODO Auto-generated method stub

  }

  @Override
  protected void onTradeRequest(TradeRequest tradeRequest) {
    // TODO Auto-generated method stub

  }

}
