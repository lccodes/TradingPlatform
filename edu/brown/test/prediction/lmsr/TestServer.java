package brown.test.prediction.lmsr;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Account;
import brown.auctions.IMarket;
import brown.messages.Registration;
import brown.securities.mechanisms.lmsr.LMSR;
import brown.securities.mechanisms.lmsr.LMSRBackend;
import brown.server.AgentServer;
import brown.setup.Logging;

import com.esotericsoftware.kryonet.Connection;

public class TestServer extends AgentServer {
	/**
	 * ID, b
	 */
	private final LMSRBackend BACKEND = new LMSRBackend(66, 10);

	public TestServer(int port) {
		super(port, new GameSetup());
	}

	@Override
	protected void onRegistration(Connection connection,
			Registration registration) {
		super.onRegistration(connection, registration);

		Account oldAccount = bank.get(connections.get(connection));
		Account newAccount = oldAccount.addAll(100, null);
		bank.put(connections.get(connection), newAccount);

		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
	}

	public void closePM(boolean yes) {
		this.exchange.close(this, 1, new TestState(yes));
		this.exchange.close(this, 2, new TestState(yes));
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
		this.exchange.open(new LMSR(1, true, BACKEND, true));
		this.exchange.open(new LMSR(2, false, BACKEND, true));
		System.out.println("[-] markets added");
		
		delay(2);
	    
	    Logging.log("Start!");
	    for (IMarket market : this.exchange.getAuctions()) {
	    	this.sendMarketUpdate(market);
	    	//this.theServer.sendToAllTCP(new MarketUpdate(0, market, market.getMechanismType()));
	    }
	    
	    delay(3);
	    TestState endState = new TestState(false);
	    this.exchange.close(this, 1, endState);
	    this.exchange.close(this, 2, endState);
	}

}
