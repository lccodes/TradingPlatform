package brown.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import brown.messages.Bid;
import brown.messages.PurchaseRequest;
import brown.messages.Registration;
import brown.messages.Trade;
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
		this.connections = new ConcurrentHashMap<Connection,Integer>();
		this.privateToPublic = new ConcurrentHashMap<Integer, Integer>();
		this.bank = new ConcurrentHashMap<Integer, Account>();
		this.pendingTradeRequests = new CopyOnWriteArrayList<TradeRequest>();
		this.privateToPublic.put(-1, -1);
		
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
	            } else if (message instanceof Trade) {
	            	aServer.onTrade(connection, (Trade) message);
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
		if (privateID != -1) {
			theServer.sendToAllTCP(tr);
		} else {
			theServer.sendToTCP(connection.getID(), tr);
		}
		pendingTradeRequests.add(tr);
	}
	
	protected void onTrade(Connection connection, Trade trade) {
		Integer privateTo = connections.get(connection);
		Integer privateFrom = publicToPrivate(trade.tradeRequest.fromID);
		if (privateFrom == null) {
			return;
		}
		if (pendingTradeRequests.contains(trade.tradeRequest)) {
			if (!trade.accept) {
				if (privateToPublic.get(privateTo) == trade.tradeRequest.fromID || 
						privateToPublic.get(privateTo) == trade.tradeRequest.toID) {
					pendingTradeRequests.remove(trade.tradeRequest);
				}
			} else if (privateToPublic.get(privateTo) == trade.tradeRequest.toID 
					|| trade.tradeRequest.toID == -1) {
				Account toAccount = bank.get(privateTo);
				Account fromAccount = bank.get(privateFrom);
				
				if (trade.tradeRequest.isSatisfied(toAccount, fromAccount)) {
					Account middleTo = toAccount.remove(trade.tradeRequest.moniesRequested,
							trade.tradeRequest.sharesRequested);
					Account newTo = middleTo.add(trade.tradeRequest.moniesOffered,
							trade.tradeRequest.sharesOffered);
					
					Account middleFrom = fromAccount.remove(trade.tradeRequest.moniesOffered,
							trade.tradeRequest.sharesOffered);
					Account newFrom = middleFrom.add(trade.tradeRequest.moniesRequested,
							trade.tradeRequest.sharesRequested);
					
					bank.put(privateTo, newTo);
					bank.put(privateFrom, newFrom);
					
					List<Integer> ids = new LinkedList<Integer>();
					ids.add(privateTo);
					ids.add(privateFrom);
					sendBankUpdates(ids);
				}
			}
		}
	}

	protected void onPurchaseRequest(Connection connection, Integer privateID, PurchaseRequest purchaseRequest) {
		// TODO Auto-generated method stub
	}

	protected void onBid(Connection connection, Integer privateID, Bid bid) {
		// TODO Auto-generated method stub
	}
	
	protected void sendBankUpdates(List<Integer> IDs) {
		synchronized(IDs) {
			for (Integer ID : IDs) {
				Connection connection = privateToConnection(ID);
				if (connection == null) {
					continue;
				}
				theServer.sendToTCP(connection.getID(), bank.get(ID));
			}
		}
	}
	
	protected Integer publicToPrivate(Integer id) {
		for (Entry<Integer, Integer> ptp : privateToPublic.entrySet()) {
			if (ptp.getValue() == id) {
				return ptp.getKey();
			}
		}
		
		return null;
	}
	
	protected Connection privateToConnection(Integer id) {
		for (Entry<Connection, Integer> ctp : connections.entrySet()) {
			if (ctp.getValue() == id) {
				return ctp.getKey();
			}
		}
		
		return null;
	}
	
	public static void main(String args) {
		// TODO server main
	}

}
