package brown.test.prediction.cda;

import brown.assets.accounting.Account;
import brown.assets.value.FullType;
import brown.assets.value.Security;
import brown.assets.value.SecurityType;
import brown.auctions.crules.ClosestMatchClearing;
import brown.auctions.twosided.TwoSidedAuction;
import brown.exceptions.AgentCreationException;
import brown.messages.Registration;
import brown.securities.mechanisms.cda.ContinuousDoubleAuction;
import brown.server.AgentServer;
import brown.setup.Logging;

import com.esotericsoftware.kryonet.Connection;

public class TestServer extends AgentServer {
	private final int ID = 1;
	private final FullType TYPENO = new FullType(SecurityType.PredictionNo, ID);
	private final FullType TYPEYES = new FullType(SecurityType.PredictionYes, ID);
	private boolean yes, no = false;

	public TestServer(int port) {
		super(port, new GameSetup());
	}
	
	@Override
	protected void onRegistration(Connection connection,
			Registration registration) {
		super.onRegistration(connection, registration);

		Account oldAccount = bank.get(connections.get(connection));
		Security newSec = null;
		if (Math.random() < .5 && !yes) {
			newSec = new Security(oldAccount.ID, 1, TYPEYES,
					state -> state.getState() == 1 ? new Account(null).add(1) : null);
			yes = true;
		} else if (Math.random() < .5 && !no) {
			newSec = new Security(oldAccount.ID, 1, TYPENO,
					state -> state.getState() == 0 ? new Account(null).add(1) : null);
			no = true;
		}
		Account newAccount = oldAccount.add(100, newSec);
		bank.put(connections.get(connection), newAccount);

		this.sendBankUpdate(connections.get(connection), oldAccount, newAccount);
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
	
	public void runGame() {
		this.exchange.open(new ContinuousDoubleAuction(0, TYPENO, new ClosestMatchClearing()));
		this.exchange.open(new ContinuousDoubleAuction(1, TYPEYES, new ClosestMatchClearing()));
		Logging.log("[-] markets open");
		delay(1);
		
		Logging.log("[-] sent market updates");
	    for (TwoSidedAuction market : this.exchange.getAuctions()) {
	    	this.sendMarketUpdate(market);
	    }
	    
	    delay(2);
	    
	    TestState endState = new TestState(false);
	    this.exchange.close(this, 0, endState);
	    this.exchange.close(this, 1, endState);
	}
	
	public static void main(String[] args) throws AgentCreationException {
		TestServer serv = new TestServer(9922);
		
		for (int i =0; i <3; i++) {
			new TestAgent("localhost", 9922);
		}
		
		serv.runGame();
		while(true){}
	}

}
