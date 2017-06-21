package brown.server.library;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Account;
import brown.markets.IMarket;
import brown.markets.LMSRBackend;
import brown.markets.LMSRServer;
import brown.messages.Registration;
import brown.server.AgentServer;
import brown.setup.Logging;
import brown.setup.library.LMSRGameSetup;

import com.esotericsoftware.kryonet.Connection;

public class TestServerLMSR extends AgentServer {
	/**
	 * ID, b
	 */
	private final LMSRBackend BACKEND = new LMSRBackend(66, 10);

	public TestServerLMSR(int port) {
		super(port, new LMSRGameSetup());
	}

	@Override
	protected void onRegistration(Connection connection,
			Registration registration) {
		super.onRegistration(connection, registration);

		Account oldAccount = acctManager.getAccount(connections.get(connection));
		Account newAccount = oldAccount.addAll(100, null);
		acctManager.setAccount(connections.get(connection), newAccount);

		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
	}

	public void closePM(boolean yes) {
		this.manager.close(this, 1, new TestStateCDA(yes));
		this.manager.close(this, 2, new TestStateCDA(yes));
	}
	
	private void delay(int amt) {
	    int i = 0;
	    while (i < amt) {
	      try {
	        Thread.sleep(1000);
	        Logging.log("[-] pause phase " + i++);
	      } catch (InterruptedException e) {
	        Logging.log("[+] woken: " + e.getMessage());
	      }
	    }
	}

	public void startGame() {
		this.manager.open(new LMSRServer(1, true, BACKEND, true));
		this.manager.open(new LMSRServer(2, false, BACKEND, true));
		System.out.println("[-] markets added");
		
		delay(2);
	    
	    Logging.log("Start!");
	    for (IMarket market : this.manager.getAuctions()) {
	    	this.sendMarketUpdate(market);
	    	//this.theServer.sendToAllTCP(new MarketUpdate(0, market, market.getMechanismType()));
	    }
	    
	    delay(3);
	    TestStateCDA endState = new TestStateCDA(false);
	    this.manager.close(this, 1, endState);
	    this.manager.close(this, 2, endState);
	}

}
