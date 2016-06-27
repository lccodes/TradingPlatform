package brown.test;

import java.util.LinkedList;

import brown.messages.Registration;
import brown.server.Account;
import brown.server.AgentServer;
import brown.server.Share;

import com.esotericsoftware.kryonet.Connection;

public class TestServer extends AgentServer {
	
	protected void onRegistration(Connection connection, Registration registration) {
		super.onRegistration(connection, registration);
		
		Account oldAccount = bank.get(connections.get(connection));
		LinkedList<Share> shareList = new LinkedList<Share>();
		shareList.add(new TestShare());
		Account newAccount = oldAccount.add(100, shareList);
		
		bank.put(connections.get(connection), newAccount);
	}

}
