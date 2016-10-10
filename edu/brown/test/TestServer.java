package brown.test;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Account;
import brown.messages.Registration;
import brown.securities.SecurityFactory;
import brown.securities.SecurityWrapper;
import brown.securities.prediction.PMLedger;
import brown.securities.prediction.PMTriple;
import brown.server.AgentServer;

import com.esotericsoftware.kryonet.Connection;

public class TestServer extends AgentServer {
	
	public TestServer(int port) {
		super(port);
		GameSetup.setup(this.theServer.getKryo());
	}

	private final double B = 1.0;
	
	@Override
	protected void onRegistration(Connection connection, Registration registration) {
		super.onRegistration(connection, registration);
		
		Account oldAccount = bank.get(connections.get(connection));
		Account newAccount = oldAccount.add(100, null);
		bank.put(connections.get(connection), newAccount);
		System.out.println(oldAccount);
		
		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
		
		List<SecurityWrapper> ms = new LinkedList<SecurityWrapper>();
		ms.add(this.exchange.getSecurity(1).wrap());
		ms.add(this.exchange.getSecurity(2).wrap());
		this.sendAllMarketUpdates(ms);
	}
	
	public void closePM(boolean yes) {
		this.exchange.close(this, 1, yes);
	}
	
	public void startGame() {
		PMTriple triple = SecurityFactory.makePM(1, 2, B);
		PMLedger yesLedger = new PMLedger(null);
		PMLedger noLedger = new PMLedger(yesLedger);
		yesLedger.setLedger(noLedger);
		this.exchange.open(triple.yes, yesLedger);
		this.exchange.open(triple.no, noLedger);
		System.out.println("[-] markets added");
	}

}
