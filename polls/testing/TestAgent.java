package testing;

import messages.PollMessage;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.Rejection;
import brown.messages.markets.MarketUpdate;
import brown.securities.SecurityWrapper;
import agent.PollAgent;

public class TestAgent extends PollAgent {

  public TestAgent(String host, int port) throws AgentCreationException {
    super(host, port);
  }

  @Override
  protected void onRejection(Rejection message) {
    System.out.println("On rejection: " + message);
  }

  @Override
  protected void onMarketUpdate(MarketUpdate marketUpdate) {
    System.out.println("new market");
    for (SecurityWrapper sec : marketUpdate.MARKETS) {
    	sec.buy(this, 1);
    	break;
    }
  }

  @Override
  protected void onBankUpdate(BankUpdate bankUpdate) {
    System.out.println("old monies: " + bankUpdate.oldAccount.monies);
    System.out.println("monies received: " + bankUpdate.newAccount.monies);
  }
  
  @Override
  protected void onPollMessage(PollMessage message) {
    System.out.println("new poll: " + message.polls.get(0).prob);
  }
  
  public static void main(String[] args) throws AgentCreationException {
    new TestAgent("localhost", 9999);
    while(true){}
  }
}
