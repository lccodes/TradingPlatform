package brown.test.auction;

import org.junit.Test;

import brown.exceptions.AgentCreationException;

public class AuctionAgentTest {

  @Test(expected = AgentCreationException.class)
  public void testMain() throws AgentCreationException {
    new AuctionAgent("1", 1);
  }

  @Test
  public void testAgent() throws AgentCreationException {
    new AuctionServer(9999);
    for (int i = 0; i < 100; i++) {
      new AuctionAgent("localhost", 9999);
    }
  }
  
  @Test
  public void simpleAuction() throws AgentCreationException {
    AuctionServer theServer = new AuctionServer(9991);
    for (int i = 0; i < 100; i++) {
      new AuctionAgent("localhost", 9991);
    }
    
    theServer.runGame();
  }

}
