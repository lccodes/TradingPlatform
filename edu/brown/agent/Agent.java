package brown.agent;

import java.io.IOException;

import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.BidRequest;
import brown.messages.MarketUpdate;
import brown.messages.Registration;
import brown.messages.TradeRequest;
import brown.setup.Startup;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/*
 * All bidding agents will implement this class
 * It abstracts away the communication issues and let's authors focus on writing bidding logic.
 */
public abstract class Agent {
	public final Client CLIENT;
	public Integer ID;
	
	/*
	 * Implementations should always invoke super()
	 */
	public Agent(String host, int port) throws AgentCreationException {
		this.CLIENT = new Client();
		this.ID = null;
		
	    CLIENT.start();
	    Kryo agentKryo = CLIENT.getKryo();
		Startup.start(agentKryo);
	    try {
			CLIENT.connect(5000, host, port, port);
		} catch (IOException e) {
			throw new AgentCreationException("Failed to connect to server");
		}
	    
		
		Agent agent = this;
		CLIENT.addListener(new Listener() {
		       public void received (Connection connection, Object message) {
		    	   synchronized(agent) {
			    	   if (message instanceof BankUpdate) {
			    		   agent.onBankUpdate((BankUpdate) message);
			    	   } else if (message instanceof BidRequest) {
			    		   agent.onBidRequest((BidRequest) message);
			    	   } else if (message instanceof TradeRequest) {
			    		   agent.onTradeRequest((TradeRequest) message);
			    	   } else if (message instanceof Registration) {
			    		   Registration reg = (Registration) message;
			    		   agent.ID = reg.getID();
			    	   } else if (message instanceof MarketUpdate) {
			    		   agent.onMarketUpdate((MarketUpdate) message);
			    	   }
		    	   }
		       }
		});
		
		CLIENT.sendTCP(new Registration(-1));
	}
	
	protected abstract void onMarketUpdate(MarketUpdate marketUpdate);

	/*
	 * Whenever an agent's bank changes, the server sends a bank update
	 * @param bankUpdate - contains the old bank state and new bank state
	 * note: both accounts provided are immutable
	 */
	protected abstract void onBankUpdate(BankUpdate bankUpdate);
	
	/*
	 * Whenever an auction is occuring, the server will request a bid
	 * using this method and provide information about the auction as
	 * a part of the request
	 * @param bidRequest - auction metadata
	 */
	protected abstract void onBidRequest(BidRequest bidRequest);
	
	/*
	 * Whenever another agent requests a trade either directly with this
	 * agent or to all agents, this method is invoked with the details
	 * of the trade. 
	 * @param tradeRequest - from fields describe what this agent will recieve
	 * and to fields describe what it will give up
	 */
	protected abstract void onTradeRequest(TradeRequest tradeRequest);
}
