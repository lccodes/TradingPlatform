package testing;

import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.MarketUpdate;
import brown.messages.Rejection;
import agent.PollAgent;

public class TestAgent extends PollAgent {

  public TestAgent(String host, int port) throws AgentCreationException {
    super(host, port);
  }

  @Override
  protected void onRejection(Rejection message) {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected void onMarketUpdate(MarketUpdate marketUpdate) {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected void onBankUpdate(BankUpdate bankUpdate) {
    // TODO Auto-generated method stub
    
  }
  
  public static void main(String[] args) throws AgentCreationException {
    new TestAgent("localhost", 9999);
    while(true){}
  }
}
