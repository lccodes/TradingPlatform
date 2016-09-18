package brown.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import brown.assets.Account;
import brown.markets.PM;
import brown.messages.Registration;
import brown.server.AgentServer;

import com.esotericsoftware.kryonet.Connection;

public class TestServer extends AgentServer {
	
	public TestServer(int port) {
		super(port);
	}

	private final double B = 1.0;
	
	private Map<String, PM> predictionmarket;
	
	@Override
	protected void onRegistration(Connection connection, Registration registration) {
		super.onRegistration(connection, registration);
		
		Account oldAccount = bank.get(connections.get(connection));
		Account newAccount = oldAccount.add(100, null);
		bank.put(connections.get(connection), newAccount);
		
		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
	}
	
	public void startGame() {
		predictionmarket = new HashMap<String, PM>();
		predictionmarket.put("test", new TestPM(B));
	}

}
