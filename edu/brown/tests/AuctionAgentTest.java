package brown.tests;

import static org.junit.Assert.fail;

import org.junit.Test;

import brown.agent.library.AuctionAgent;
import brown.exceptions.AgentCreationException;
import brown.server.library.AuctionServer;

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
  public void simpleOutcryAuction() throws AgentCreationException {
    AuctionServer theServer = new AuctionServer(9991);
    for (int i = 0; i < 100; i++) {
      new AuctionAgent("localhost", 9991);
    }
    
    double cost = theServer.runGame(true, false);
    if ((cost - theServer.MAX) > 1) {
    	fail();
    }
  }
  
  @Test
  public void simpleSealedAuction() throws AgentCreationException {
    AuctionServer theServer = new AuctionServer(9992);
    for (int i = 0; i < 50; i++) {
      new AuctionAgent("localhost", 9992);
    }
    
    double cost = theServer.runGame(false, true);
    if ((cost - theServer.MAX) > 1) {
    	fail();
    }
  }
  
  @Test
  public void simpleSealedSecondPriceAuction() throws AgentCreationException {
    AuctionServer theServer = new AuctionServer(9993);
    for (int i = 0; i < 50; i++) {
      new AuctionAgent("localhost", 9993);
    }
    
    double cost = theServer.runGame(false, false);
    if ((cost - theServer.MAX) > 1) {
    	fail();
    }
  }

}
