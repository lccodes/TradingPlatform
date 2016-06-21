package brown.agent;

import java.io.IOException;

import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.BidRequest;
import brown.messages.TradeRequest;
import brown.setup.Startup;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public abstract class Agent {
	
	public Agent(String host, int port) throws AgentCreationException {
		Client client = new Client();
	    client.start();
	    try {
			client.connect(5000, host, port, port);
		} catch (IOException e) {
			throw new AgentCreationException("Failed to connect to server");
		}
	    Kryo agentKryo = client.getKryo();
		Startup.start(agentKryo);
		
		Agent agent = this;
		client.addListener(new Listener() {
		       public void received (Connection connection, Object message) {
		    	   if (message instanceof BankUpdate) {
		    		   agent.onBankUpdate((BankUpdate) message);
		    	   } else if (message instanceof BidRequest) {
		    		   agent.onBidRequest((BidRequest) message);
		    	   } else if (message instanceof TradeRequest) {
		    		   agent.onTradeRequest((TradeRequest) message);
		    	   }
		       }
		});
	}
	
	protected abstract void onBankUpdate(BankUpdate bankUpdate);
	protected abstract void onBidRequest(BidRequest bidRequest);
	protected abstract void onTradeRequest(TradeRequest tradeRequest);
}