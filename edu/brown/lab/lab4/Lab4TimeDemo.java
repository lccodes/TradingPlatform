package brown.lab.lab4;

import brown.exceptions.AgentCreationException;
import brown.lab.UnitCDAWrapper;
import brown.messages.markets.GameReport;
import brown.setup.Logging;

public class Lab4TimeDemo extends Lab4Agent {
	private UnitCDAWrapper market;

	public Lab4TimeDemo(String host, int port) throws AgentCreationException {
		super(host, port);
		this.market = null;
	}

	@Override
	protected void onContinuousDoubleAuction(UnitCDAWrapper unitCDAWrapper) {
		// Update my latest market
		this.market = unitCDAWrapper;
	}

	@Override
	public void onMarketUpdate(GameReport marketUpdate) {
		// ignore market reports
	}
	
	/**
	 * My logic for bidding every second
	 * once the market has opened
	 * regardless of what's up
	 */
	public void bidForever() {
		// Keep bidding forever
		while(true) {
			try {
				// Only bid once the market has opened
				if (this.market != null) {
					// Buy for 10
					market.buy(this, 10);
				}
				// Wait 5 seconds
				Thread.sleep(5000);
				Logging.log("[-] pause phase");
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}
	}
	
	// Throw the exception since there's no recovery
	public static void main(String[] args) throws AgentCreationException {
		// We're running the server on this machine with port 2121
		new Lab4TimeDemo("localhost", 2121)
		// Start my auto bidding process
			.bidForever();
	}

}
