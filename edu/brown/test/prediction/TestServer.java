package brown.test.prediction;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Account;
import brown.messages.Registration;
import brown.securities.Security;
import brown.securities.SecurityFactory;
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
		Account newAccount = oldAccount.addAll(100, null);
		bank.put(connections.get(connection), newAccount);
		System.out.println(oldAccount);
		
		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
		
		List<Security> ms = new LinkedList<Security>();
		ms.add(this.exchange.getSecurity(1));
		ms.add(this.exchange.getSecurity(2));
		this.sendAllMarketUpdates(ms);
	}
	
	public void closePM(boolean yes) {
		this.exchange.close(this, 1, yes);
	}
	
	public void startGame() {
		PMTriple triple = SecurityFactory.makePM(1, 2, B);
		this.exchange.open(triple.yes, triple.ledgerYes);
		this.exchange.open(triple.no, triple.ledgerNo);
		System.out.println("[-] markets added");
	}

}
