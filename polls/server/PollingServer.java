package server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import brown.assets.accounting.Account;
import brown.messages.Registration;
import brown.server.AgentServer;
import brown.test.GameSetup;

import com.esotericsoftware.kryonet.Connection;

public class PollingServer extends AgentServer {
  private Map<Integer, String> idToState;
  private Set<String> states;
  private Map<String, Integer> stateToVote;

  public PollingServer(int port) {
    super(port);
    this.idToState = new ConcurrentHashMap<Integer, String>();
    this.states = new HashSet<String>(Arrays.asList("California", "New York", "Florida"));
    this.stateToVote = new ConcurrentHashMap<String, Integer>();
    this.stateToVote.put("California", 55);
    this.stateToVote.put("New York", 29);
    this.stateToVote.put("Florida", 29);
    GameSetup.setup(this.theServer.getKryo());
  }
  
  @Override
  protected void onRegistration(Connection connection, Registration registration) {
    if (this.states.size() == 0) {
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
      Account newAccount = account.add(this.stateToVote.get(this.idToState.get(ID)), null);
      this.bank.put(ID, newAccount);
      this.sendBankUpdate(ID, account, newAccount);
    }
  }

}
