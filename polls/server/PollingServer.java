package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import setup.GameSetup;
import messages.Poll;
import messages.PollMessage;
import brown.assets.accounting.Account;
import brown.messages.Registration;
import brown.messages.Rejection;
import brown.messages.markets.PurchaseRequest;
import brown.securities.Security;
import brown.securities.SecurityFactory;
import brown.securities.prediction.PMTriple;
import brown.server.AgentServer;

import com.esotericsoftware.kryonet.Connection;

public class PollingServer extends AgentServer {
  private Map<Integer, String> idToState;
  private Set<String> states;
  private Map<String, Integer> stateToVote;
  private final int CAP;
  private final double TRUE;
  private boolean round;
  
  private final Random random;
  private final AtomicBoolean marketOpen;

  public PollingServer(int port, int cap) {
    super(port);
    GameSetup.setup(this.theServer.getKryo());
    
    this.random = new Random();
    this.CAP = cap;
    this.idToState = new ConcurrentHashMap<Integer, String>();
    this.states = new HashSet<String>(Arrays.asList("California", "New York", "Florida"));
    this.stateToVote = new ConcurrentHashMap<String, Integer>();
    this.stateToVote.put("California", 55);
    this.stateToVote.put("New York", 29);
    this.stateToVote.put("Florida", 29);
    this.round = true;
    this.marketOpen = new AtomicBoolean(false);
    this.TRUE = random.nextDouble();
    System.out.println("[-] truth: " + this.TRUE);
    
    PMTriple triple = SecurityFactory.makePM(1, 2, 1);
    this.exchange.open(triple.yes, triple.ledgerYes);
	this.exchange.open(triple.no, triple.ledgerNo);
	System.out.println("[-] markets added");
  }
  
  @Override
  protected void onRegistration(Connection connection, Registration registration) {
    if (this.states.size() == 0 || this.connections.size() == CAP) {
      System.out.println("[-] rejected " + connection.getID());
      return;
    }
    
    super.onRegistration(connection, registration);
    synchronized(this.states) {
      int item = new Random().nextInt(this.states.size());
      int i = 0;
      String toDelete = null;
      for(String state : this.states) {
        if (i == item) {
          idToState.put(this.connections.get(connection), state);
          toDelete = state;
          break;
        }
        i++;
      }
      this.states.remove(toDelete);
    }
    
    Integer ID = this.connections.get(connection);
    Account account = this.bank.get(ID);
    synchronized(account) {
      Account newAccount = account.addAll(this.stateToVote.get(this.idToState.get(ID)), null);
      this.bank.put(ID, newAccount);
      this.sendBankUpdate(ID, account, newAccount);
    }
  }
  
  @Override
  protected void onPurchaseRequest(Connection connection, Integer privateID, PurchaseRequest purchaseRequest) {
	  if (this.marketOpen.get()) {
		  super.onPurchaseRequest(connection, privateID, purchaseRequest);
		  return;
	  }
	  
	  Rejection rej = new Rejection(privateID, purchaseRequest);
	  theServer.sendToTCP(connection.getID(), rej);
  }
  
  /**
   * Runs the infinite game loop. This is in a separate thread than the message handling logic.
   */
  public void runGame() {
	  try {
		  while(true) {
			  if (this.connections.size() == CAP && round){
				  this.marketOpen.set(true);
				  List<Security> securities = Arrays.asList(this.exchange.getSecurity(1),
						  this.exchange.getSecurity(2));
				  this.sendAllMarketUpdates(securities);
				  System.out.println("[-] markets sent: true");
				  Thread.sleep(1000);
				  System.out.println("[-] polls sent: " + sendPolls(.25));
				  Thread.sleep(5000);
				  this.marketOpen.set(false);
				  System.out.println("[-] final price: " + this.exchange.getSecurity(1).bid(1));
				  
				  round = false;
			  } else {
				  Thread.sleep(500);
			  }
		  }
	  } catch (InterruptedException e) {
		  System.out.println("[x] Interuptted!");
	  }
  }

  /**
   * Method to send every agent a random poll in the range [max(0,prob-X), min(1,prob+X)]
   * where X is the double passed to this method. The counts associated will
   * be uniformly random ints.
   */
  private boolean sendPolls(double x) {
	  if (x < 0 || x > 1) {
		  return false;
	  }
	  
	  for (Connection connection : this.connections.keySet()) {
		  double prob = random.nextDouble() * x;
		  if (random.nextDouble() < .5) {
			  prob = Math.max(0, this.TRUE - prob);
		  } else {
			  prob = Math.min(1, this.TRUE + prob);
		  }
		  
		  PollMessage poll = new PollMessage(random.nextInt(), new ArrayList<Poll>(Arrays.asList(new Poll(prob, random.nextInt()))));
		  this.theServer.sendToTCP(connection.getID(), poll);
	  }
	  
	  return true;
  }

}
