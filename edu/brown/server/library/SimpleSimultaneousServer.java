package brown.server.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import brown.rules.allocationrules.SimpleClockAllocation;
import brown.rules.allocationrules.SimpleHighestBidderAllocation;
import brown.assets.accounting.Account;
import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.assets.value.TradeableType;
import brown.marketinternalstates.SimpleInternalState;
import brown.markets.Market;
import brown.markets.library.SimpleSecondPriceMarket;
import brown.messages.Registration;
import brown.rules.paymentrules.library.SimpleClockRule;
import brown.rules.paymentrules.library.SimpleSecondPrice;
import brown.registrations.PPValRegistration;
import brown.rules.activityrules.OneShotActivity;
import brown.rules.activityrules.SimpleNoJumpActivityRule;
import brown.rules.irpolicies.library.AnonymousPolicy;
import brown.rules.queryrules.library.OutcryQueryRule;
import brown.rules.queryrules.library.SealedBidQuery;
import brown.rules.terminationconditions.OneShotTermination;
import brown.rules.terminationconditions.SamePaymentsTermination;
import brown.server.AgentServer;
import brown.setup.Logging;
import brown.setup.library.LabGameSetup;
import brown.tradeables.Lab8Good;
import brown.tradeables.SimGood;
import brown.tradeables.Tradeable;
import brown.valuation.NormalValuation;
import brown.valuation.SpecValGenerator;
import brown.valuation.Valuation;
import brown.valuation.ValuationBundle;
import ch.uzh.ifi.ce.mweiss.specval.model.UnsupportedBiddingLanguageException;

import com.esotericsoftware.kryonet.Connection;

/**
 * Simultaneous good server, runs auctions with a set of goods. 
 * In this case implemented as a one-shot, second price auction.
 * @author acoggins
 *
 */
public class SimpleSimultaneousServer extends AgentServer {
  
  private Map<Integer, ValuationBundle> agentValues = new HashMap<Integer, ValuationBundle>();
  private Integer numberOfBidders;
  private Integer NUMGOODS = 8;
  private Function<Integer, Double> VALFUNCTION = x -> (double) x + 10;
  private Boolean MONOTONIC = true; 
  private Double VALUESCALE = 1.0;
  private Double BASEVARIANCE = 1.0;
  private Double EXPCOVAR = 0.0;
  private Integer NUMVALUATIONS = 8; 
  private Integer BUNDLESIZEMEAN = 4; 
  private Double BUNDLESIZESTDDEV = 1.0;
  
  /**
   * constructor for the server. Connects to port. 
   * @param port
   * port number.
   */
  public SimpleSimultaneousServer (int port) {
    super(port, new LabGameSetup());
    this.numberOfBidders = 0; 
  }
  
  @Override
  protected void onRegistration(Connection connection, Registration registration) {
    numberOfBidders++;
    Integer theID = this.defaultRegistration(connection, registration);
    if (theID == null) {
      return;
    }
    ValuationBundle values = new ValuationBundle();
    Set<FullType> allGoods = new HashSet<FullType>();
    PPValRegistration registeredValue = new PPValRegistration(theID, values, allGoods);
    this.theServer.sendToTCP(connection.getID(), registeredValue);
    Account oldAccount = acctManager.getAccount(connections.get(connection));
    acctManager.setAccount(connections.get(connection), oldAccount.addAll(10000, null));
    List<Integer> IDS = new LinkedList<Integer>();
    IDS.add(connections.get(connection));
    this.sendBankUpdates(IDS);
  }
  
  /**
   * runs the game. This involves setup phase, assigning valuations,\
   * opening and closing market(s), printing output. 
   */
  public void runGame() {
    //setups phase
    int j = 0;
    while (j < 5) { //CHANGE FOR MORE OR LESS JOIN TIME
      try {
        Thread.sleep(1000);
        Logging.log("[-] setup phase " + j++);
      } catch (InterruptedException e) {
        Logging.log("[+] woken: " + e.getMessage());
      }
    }
    //create set of goods. 
    Set<FullType> goodSet = new HashSet<FullType>();
    for(int i = 0; i < NUMGOODS; i++) {
      goodSet.add(new FullType(TradeableType.Good, i));
    }
    
    //create valuation
    NormalValuation normalValuation = new NormalValuation(goodSet, VALFUNCTION,
        BASEVARIANCE, EXPCOVAR, MONOTONIC, VALUESCALE);
    for(Entry<Connection, Integer> conn : this.connections.entrySet()) { 
      ValuationBundle aValue = normalValuation.getSomeValuations(NUMVALUATIONS,
          BUNDLESIZEMEAN, BUNDLESIZESTDDEV);
      System.out.println("val " + aValue);
      agentValues.put(conn.getValue(), aValue);
      this.theServer.sendToTCP(conn.getKey().getID(), 
          new PPValRegistration(conn.getValue(), aValue, goodSet));
    }
    
    //for the market
    Set<Tradeable> marketGoods = new HashSet<Tradeable>();
    for(int i = 0; i < NUMGOODS; i++) {
      marketGoods.add(new Lab8Good(i));
    }
    //open market
    SimpleSecondPriceMarket thisMarket = new SimpleSecondPriceMarket(0, marketGoods);
    this.manager.open(thisMarket.getMarket());
    
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
     ValuationBundle myValue = agentValues.get(account.ID);
      Double maxValue = 0.0;
      for (Valuation wantedBundle : myValue) {
        int contains = 0;
        for (Tradeable t : account.tradeables) {
          if (wantedBundle.contains(t.getType())) {
            contains++;
          }
        }
        if (contains == wantedBundle.size()) {
          maxValue = Math.max(maxValue, wantedBundle.getPrice());
        }
      }
      double linearCost = (10000 - account.monies) > 0 ? (10000 - account.monies) : account.monies - 10000;
      System.out.println(account.ID + " valued what they got at " + maxValue);
      System.out.println(account.ID + " has a linear utility of " + (maxValue - linearCost));
      System.out.println(account.ID + " has a strict budget utility of " + (maxValue - linearCost > 0 ? maxValue-linearCost : -1*Double.MAX_VALUE));
      System.out.println();
    } 
  }
  
  public static void main(String[] args) {
    SimpleSimultaneousServer s1 = new SimpleSimultaneousServer(2122);
    s1.runGame();
    
  }
}
