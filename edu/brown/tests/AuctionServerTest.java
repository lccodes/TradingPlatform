package brown.tests;

import org.junit.Test;

import brown.server.library.AuctionServer;

public class AuctionServerTest {

  @Test
  public void testRunGame() {
    new AuctionServer(1234);
  }

}
