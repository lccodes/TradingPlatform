package brown.server.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.esotericsoftware.kryonet.Connection;

import brown.assets.accounting.Account;
import brown.assets.value.FullType;
import brown.messages.Registration;
import brown.registrations.SingleValRegistration;
import brown.server.AgentServer;
import brown.setup.Logging;
import brown.setup.library.LabGameSetup;
import brown.tradeables.Tradeable;

public class SimpleSingleGoodServer extends AgentServer {
	
	//what do we need... we need agents, a good, a valuation for that good for each agent, 
	//we have agents under the agent interface. 
	//we have goods under the good interface
	// we need predictions under the price prediction interface
	
	public SimpleSingleGoodServer(int port) {
		super(port, new LabGameSetup());

	}
	
	@Override
	protected void onRegistration(Connection connection, Registration registration) {
		
		Integer theID = this.defaultRegistration(connection, registration);
		if (theID == null) {
			return;
		}
		Map<FullType, Double> value = new HashMap<FullType, Double>();
		this.theServer.sendToTCP(connection.getID(), new SingleValRegistration(theID, value));

		Account oldAccount = acctManager.getAccount(connections.get(connection));
		Account newAccount = oldAccount.addAll(10000, null);
		acctManager.setAccount(connections.get(connection), newAccount);
		
		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
	}
	
	public void runGame() {
		int i = 0;
		while (i < 10) { //CHANGE FOR MORE OR LESS JOIN TIME
			try {
				Thread.sleep(1000);
				Logging.log("[-] setup phase " + i++);
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}
		
		//here is where I will build a valuation framework. 
	}
	
	public static void main(String[] args) {
		SimpleSingleGoodServer s1 = new SimpleSingleGoodServer(2121);
		s1.runGame();
	}
	
	
	
	
	
}