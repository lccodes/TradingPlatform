package brown.lab.lab3;

import brown.agent.Agent;
import brown.auctions.bundles.SimpleBidBundle;
import brown.exceptions.AgentCreationException;
import brown.lab.GameSetup;
import brown.lab.ValuationRegistration;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.Rejection;
import brown.messages.auctions.Bid;
import brown.messages.auctions.TradeRequest;
import brown.messages.markets.MarketUpdate;
import brown.messages.trades.NegotiateRequest;
import brown.setup.Logging;

public abstract class Lab3Agent extends Agent{
  
  private double myValuation;

  public Lab3Agent(String host, int port) throws AgentCreationException {
    super(host, port,  new GameSetup());
  }
  
  @Override
  protected void onRegistration(Registration registration) {
    super.onRegistration(registration);
    ValuationRegistration valuationRegistration = (ValuationRegistration) registration;
    this.myValuation = valuationRegistration.getValue();
    Logging.log("[+] max: " + this.myValuation);
  }

  @Override
  protected void onRejection(Rejection message) {
    Logging.log("[x] rejected: " + message.failedBR);
  }

  @Override
  protected void onMarketUpdate(MarketUpdate marketUpdate) {
    //Noop
  }

  @Override
  protected void onBankUpdate(BankUpdate bankUpdate) {
    Logging.log("[-] bank " + bankUpdate.newAccount.monies);
    if (bankUpdate.newAccount.goods.size() > 0) {
      Logging.log("[+] victory!");
    }
  }

  @Override
  protected void onNegotiateRequest(NegotiateRequest tradeRequest) {
    //Noop
  }
  
  /**
   * Sends a bid to the server
   * @param bid
   */
  protected void bid(double bid, TradeRequest tr) {
    this.CLIENT.sendTCP(new Bid(0, new SimpleBidBundle(bid, this.ID, tr.BundleType), tr.AuctionID, this.ID));
  }

}
