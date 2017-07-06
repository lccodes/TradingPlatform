package brown.server.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import com.esotericsoftware.kryonet.Connection;

import brown.assets.accounting.Account;
import brown.assets.value.FullType;
import brown.assets.value.TradeableType;
import brown.markets.Market;
import brown.messages.Registration;
import brown.registrations.SingleValRegistration;
import brown.registrations.ValuationRegistration;
import brown.server.AgentServer;
import brown.setup.Logging;
import brown.setup.library.LabGameSetup;
import brown.tradeables.Tradeable;
import brown.valuation.NormalValuation;
import brown.valuation.SizeDependentValuation;
import brown.valuation.ValuationBundle;

public class SimpleSingleGoodServer extends AgentServer {
	
  private int numberOfBidders;
	//what do we need... we need agents, a good, a valuation for that good for each agent, 
	//we have agents under the agent interface. 
	//we have goods under the good interface
	// we need predictions under the price prediction interface
	
	public SimpleSingleGoodServer(int port) {
		super(port, new LabGameSetup());
		this.numberOfBidders = 0;
	}
	
	@Override
	protected void onRegistration(Connection connection, Registration registration) {
		this.numberOfBidders++;
		Integer theID = this.defaultRegistration(connection, registration);
		if (theID == null) {
			return;
		}
		ValuationBundle values = new ValuationBundle();
		ValuationRegistration registeredValue = new ValuationRegistration(theID, values);
		this.theServer.sendToTCP(connection.getID(), registeredValue);

		Account oldAccount = acctManager.getAccount(connections.get(connection));
		acctManager.setAccount(connections.get(connection), oldAccount.addAll(10000, null));
		
		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
	}
	
	public void runGame() {
		int j = 0;
		while (j < 10) { //CHANGE FOR MORE OR LESS JOIN TIME
			try {
				Thread.sleep(1000);
				Logging.log("[-] setup phase " + j++);
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}
		//here is where I will build a valuation framework. 
		//done baby
		Set<FullType> singleGood = new HashSet<FullType>();
		singleGood.add(new FullType(TradeableType.Good, 1));
		Function<Integer, Double> linear = x -> (double) x;
		NormalValuation normalVal = new NormalValuation(singleGood, linear, false, 1.0);
		for(Integer connID : this.connections.values()){ 
		  this.theServer.sendToTCP(connID, new ValuationRegistration(connID, normalVal.getAllValuations()));
		}

		this.manager.open(new Market());
		
		
		
		
		
		
		
	}
	
	public static void main(String[] args) {
		SimpleSingleGoodServer s1 = new SimpleSingleGoodServer(2121);
		s1.runGame();
	}
	
	
	
	
	
}