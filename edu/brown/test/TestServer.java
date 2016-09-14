package brown.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import brown.markets.PM;
import brown.markets.Share;
import brown.messages.Registration;
import brown.server.Account;
import brown.server.AgentServer;

import com.esotericsoftware.kryonet.Connection;

public class TestServer extends AgentServer {
	private final double B = 1.0;
	
	private Map<String, PM> predictionmarket;
	
	protected void onRegistration(Connection connection, Registration registration) {
		super.onRegistration(connection, registration);
		
		Account oldAccount = bank.get(connections.get(connection));
		LinkedList<Share> shareList = new LinkedList<Share>();
		shareList.add(new TestShare());
		Account newAccount = oldAccount.add(100, shareList);
		
		bank.put(connections.get(connection), newAccount);
	}
	
	public void startGame() {
		predictionmarket = new HashMap<String, PM>();
		predictionmarket.put("test", new TestPM(B));
	}

}
