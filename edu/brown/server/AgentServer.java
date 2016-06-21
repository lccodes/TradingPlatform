package brown.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import brown.messages.Bid;
import brown.messages.PurchaseRequest;
import brown.messages.Registration;
import brown.messages.TradeRequest;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public abstract class AgentServer {
	public static int PORT = 2222;
	
	protected Map<Connection, Integer> connections;
	protected Map<Integer, Integer> privateToPublic;
	protected Map<Integer, Account> bank;
	//Consider time limiting these
	protected List<TradeRequest> pendingTradeRequests;
	
	private int agentCount;
	
	private Server theServer;
	
	public AgentServer() {
		this.agentCount = 0;
		theServer = new Server();
	    theServer.start();
	    try {
			theServer.bind(PORT, PORT);
		} catch (IOException e) {
			System.out.println("[X] Server failed to start due to port conflict");
			return;
		}
	    
	    AgentServer aServer = this;
	    theServer.addListener(new Listener() {
	        public void received (Connection connection, Object message) {
	        	Integer id = null;
	        	if (connections.containsKey(connection)) {
	        		id = connections.get(connection);
	        	} else if (message instanceof Registration) {
	            	aServer.onRegistration(connection, (Registration) message);
	            	return;
	            } else {
	            	return;
	            }
	        	
	        	if (message instanceof Bid) {
	        		aServer.onBid(connection, id, (Bid) message);
	            } else if (message instanceof PurchaseRequest) {
	            	aServer.onPurchaseRequest(connection, id, (PurchaseRequest) message);
	            } else if (message instanceof TradeRequest) {
	            	aServer.onTradeRequest(connection, id, (TradeRequest) message);
	            }
	        }
	    });
	}

	protected void onRegistration(Connection connection, Registration registration) {
		if (registration.getID() == null) {
			return;
		}
		
		connections.put(connection, registration.getID());
		//TODO: Consider removing old connection if present
		if (!privateToPublic.containsKey(registration.getID())) {
			privateToPublic.put(registration.getID(), agentCount++);
			bank.put(registration.getID(), new Account(registration.getID()));
		}
	}

	protected void onTradeRequest(Connection connection, Integer privateID, TradeRequest tradeRequest) {
		TradeRequest tr = tradeRequest.safeCopy(privateToPublic.get(privateID));
		theServer.sendToTCP(connection.getID(), tr);
		pendingTradeRequests.add(tr);
	}
	
	//TODO: Add onTrade. If either party rejects, remove it from list. 

	protected void onPurchaseRequest(Connection connection, Integer privateID, PurchaseRequest purchaseRequest) {
		// TODO Auto-generated method stub
	}

	protected void onBid(Connection connection, Integer privateID, Bid bid) {
		// TODO Auto-generated method stub
	}
	
	public static void main(String args) {
		// TODO server main
	}

}
