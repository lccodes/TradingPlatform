package brown.server.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import brown.assets.accounting.Account;
import brown.assets.value.BasicType;
import brown.assets.value.TradeableType;
import brown.generator.library.NormalGenerator;
import brown.markets.Market;
import brown.markets.library.SimpleSecondPriceMarket;
import brown.messages.Registration;
import brown.registrations.PPValRegistration;
import brown.server.AgentServer;
import brown.setup.Logging;
import brown.setup.library.LabGameSetup;
import brown.tradeables.Lab8Good;
import brown.tradeables.Tradeable;
import brown.valuable.library.Bundle;
import brown.valuable.library.Good;
import brown.valuation.IValuation;
import brown.valuation.IValuationSet;
import brown.valuation.library.BundleValuation;
import brown.valuation.library.BundleValuationSet;

import com.esotericsoftware.kryonet.Connection;

/**
 * Simultaneous good server, runs auctions with a set of goods. 
 * In this case implemented as a one-shot, second price auction.
 * @author acoggins
 *
 */
public class SimpleSimultaneousServer extends AgentServer {
  
  private Map<Integer, IValuationSet> agentValues = new HashMap<Integer, IValuationSet>();
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
    Set<BasicType> allGoods = new HashSet<BasicType>();
    Map<Set<BasicType>, Double> values = new HashMap<Set<BasicType>, Double>();
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
    while (j < 10) { //CHANGE FOR MORE OR LESS JOIN TIME
      try {
        Thread.sleep(1000);
        Logging.log("[-] setup phase " + j++);
      } catch (InterruptedException e) {
        Logging.log("[+] woken: " + e.getMessage());
      }
    }
    //create set of goods. 
    Set<BasicType> typeSet = new HashSet<BasicType>();
    Set<Good> goodSet = new HashSet<Good>(); 
    for(int i = 0; i < NUMGOODS; i++) {
     typeSet.add(new BasicType(TradeableType.Good, i));
     goodSet.add(new Good(i));
    }
    
    //create valuation
    NormalGenerator normalValuation = new NormalGenerator(VALFUNCTION,
        BASEVARIANCE, EXPCOVAR, MONOTONIC, VALUESCALE);
    for(Entry<Connection, Integer> conn : this.connections.entrySet()) { 
      BundleValuationSet aValue = normalValuation.getSomeBundleValuations(
          new Bundle(goodSet), NUMVALUATIONS,
          BUNDLESIZEMEAN, BUNDLESIZESTDDEV);
      System.out.println("val " + aValue);
      agentValues.put(conn.getValue(), aValue);
      this.theServer.sendToTCP(conn.getKey().getID(), 
          new PPValRegistration(conn.getValue(), convert(aValue), typeSet));
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
     BundleValuationSet myValue = (BundleValuationSet) agentValues.get(account.ID);
      Double maxValue = 0.0;
      for (IValuation wantedBundle : myValue) {
        BundleValuation wantedBundleTwo = (BundleValuation) wantedBundle;
        int contains = 0;
        for (Tradeable t : account.tradeables) {
          Good corresponding = new Good(t.getType().ID);
          if (wantedBundleTwo.contains(corresponding)) {
            contains++;
          }
        }
        if (contains == wantedBundleTwo.size()) {
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
  
  /**
   * converts from a bundleValuationSet to a map of set of basictype
   * @param bvs
   * @return
   */
  private Map<Set<BasicType>, Double> convert(BundleValuationSet bvs) {
    Map<Set<BasicType>, Double>  valMap = new HashMap<Set<BasicType>, Double>();
    for(IValuation b : bvs) {
      BundleValuation newB = (BundleValuation) b;
      Set<BasicType> basic = new HashSet<BasicType>();
      Bundle bund = (Bundle) b.getValuable();
      for(Good g : bund.bundle)
        basic.add(new BasicType(TradeableType.Good, g.ID));
      valMap.put(basic, b.getPrice());
    }
    return valMap;
  }
  
  public static void main(String[] args) {
    SimpleSimultaneousServer s1 = new SimpleSimultaneousServer(2122);
    s1.runGame();
    
  }
}
