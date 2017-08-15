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
import brown.assets.value.BasicType;
import brown.assets.value.TradeableType;
import brown.generator.library.NormalGenerator;
import brown.markets.Market;
import brown.markets.library.SimpleSecondPriceMarket;
import brown.messages.Registration;
import brown.registrations.SingleValRegistration;
import brown.server.AgentServer;
import brown.setup.Logging;
import brown.setup.library.LabGameSetup;
import brown.tradeables.Lab8Good;
import brown.tradeables.Tradeable;
import brown.valuable.library.Good;
import brown.valuation.library.AdditiveValuation;


/**
 * A sever for a single good game. 
 * Implemented with a single good, one-shot second price auction.S
 * @author acoggins
 *
 */
public class SimpleSingleGoodServer extends AgentServer {
	
  private int numberOfBidders;
  private Map<Integer, AdditiveValuation> agentValues = new HashMap<Integer, AdditiveValuation>();
  private Function<Integer, Double> VALFUNCTION = x -> (double)x + 10;
  
  /**
   * constructor for the single good server. 
   * @param port
   * the port of the computer on which the game is run.
   */
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
		SingleValRegistration registeredValue = 
		    new SingleValRegistration(0, new BasicType(TradeableType.Good, 0), 0.0);
		this.theServer.sendToTCP(connection.getID(), registeredValue);

		Account oldAccount = acctManager.getAccount(connections.get(connection));
		acctManager.setAccount(connections.get(connection), oldAccount.addAll(10000, null));
		
		List<Integer> IDS = new LinkedList<Integer>();
		IDS.add(connections.get(connection));
		this.sendBankUpdates(IDS);
	}
	
	/**
	 * the method that handles running the game. This includes opening and closing markets, 
	 * setup phase, creating valuations, and printing outcomes.
	 */
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
		//create good
		BasicType singleType = new BasicType(TradeableType.Good, 0);
		Good singleGood = new Good(0);
		//get valuation
		NormalGenerator normalVal = new NormalGenerator(VALFUNCTION, false, 1.0);

		for(Entry<Connection, Integer> conn : this.connections.entrySet()) { 
		  //check on this
	    AdditiveValuation aValue = normalVal.getSingleValuation(singleGood);
	    System.out.println("val " + aValue);
	    agentValues.put(conn.getValue(), aValue);
		  this.theServer.sendToTCP(conn.getKey().getID(),
		      new SingleValRegistration(conn.getValue(), singleType, aValue.getPrice()));
		}
		//for the market
		Set<Tradeable> goodSet = new HashSet<Tradeable>();
		goodSet.add(new Lab8Good(0));
		//create market presets
		SimpleSecondPriceMarket thisMarket = new SimpleSecondPriceMarket(0, goodSet);
		this.manager.open(thisMarket.getMarket());
		
		//now call it
		Market market = this.manager.getIMarket(0);
		while(!market.isOver()) {
		  try {
        Thread.sleep(4000);
        this.updateAllAuctions(true);
      } catch (InterruptedException e) {
        Logging.log("[+] woken: " + e.getMessage());
      }
		}
	  this.updateAllAuctions(true);
	  
	  System.out.println("\n\n\n\n\nOUTCOME:");
    for (Account account : this.acctManager.getAccounts()) {
      System.out.println(account);
      System.out.println(account.ID + " got " + account.tradeables.size() + " items with an average cost of "
          + (10000 - account.monies) / account.tradeables.size());
     AdditiveValuation myValue = agentValues.get(account.ID);
      double linearCost = (10000 - account.monies) > 0 ? (10000 - account.monies) : account.monies - 10000;
      System.out.println(account.ID + " valued what they got at " + myValue.getPrice());
      System.out.println(account.ID + " has a linear utility of " + (myValue.getPrice() - linearCost));
      System.out.println(account.ID + " has a strict budget utility of " + 
      (myValue.getPrice() - linearCost > 0 ? myValue.getPrice()-linearCost : -1* Double.MAX_VALUE));
      System.out.println();
    }	
	}
	
	/**
	 * runnable main method of the auction.
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleSingleGoodServer s1 = new SimpleSingleGoodServer(2122);
		s1.runGame();
	}
}