package testing;

import server.PollingServer;

public class LaunchTestGame {
  public static void main(String[] args) {
    new PollingServer(9999, 3)
    	.runGame();
  }
}
