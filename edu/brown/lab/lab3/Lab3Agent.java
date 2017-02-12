package brown.lab.lab3;

import brown.agent.Agent;
import brown.exceptions.AgentCreationException;
import brown.lab.ValuationRegistration;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.Rejection;
import brown.messages.markets.MarketUpdate;
import brown.messages.trades.NegotiateRequest;
import brown.setup.Logging;
import brown.test.auction.GameSetup;

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

}
