package brown.lab.lab5;

import brown.server.AgentServer;
import brown.setup.Logging;
import brown.setup.Setup;

public class Lab5Server extends AgentServer {

	public Lab5Server(int port, Setup gameSetup) {
		super(port, gameSetup);
	}
	
	private void delay(int amt, boolean update) {
		int i = 0;
		while (i < amt) {
			try {
				if (update) {
					this.updateAllAuctions();
				}
				Thread.sleep(1000);
				Logging.log("[-] pause phase " + i++);
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}
	}
	
	public void runGame() {
		delay(10, false);
		// Add auction with lemonade rule
		delay(2, true);
		// Run close market on it
	}

}
