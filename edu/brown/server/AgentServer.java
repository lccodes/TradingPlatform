package brown.server;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import brown.assets.accounting.Account;
import brown.assets.accounting.Exchange;
import brown.assets.accounting.Ledger;
import brown.assets.accounting.Order;
import brown.assets.value.Tradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.onesided.OneSidedAuction;
import brown.auctions.twosided.TwoSidedAuction;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.Rejection;
import brown.messages.auctions.Bid;
import brown.messages.auctions.TradeRequest;
import brown.messages.markets.LimitOrder;
import brown.messages.markets.MarketUpdate;
import brown.messages.markets.PurchaseRequest;
import brown.messages.trades.NegotiateRequest;
import brown.messages.trades.Trade;
import brown.setup.Logging;
import brown.setup.Setup;
import brown.setup.Startup;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/**
 * This is the server that all trading agent games will implement. It abstracts
 * away all of the communication logic and largely the shared market structures
 * (auctions, markets, trading) so that designers can focus on their game
 * specifics
 */
public abstract class AgentServer {
	protected Map<Connection, Integer> connections;
	protected Map<Integer, Integer> privateToPublic;
	protected Map<Integer, Account> bank;
	// Consider time limiting these
	protected List<NegotiateRequest> pendingTradeRequests;
	protected Exchange exchange;
	protected Map<Integer, OneSidedAuction> auctions;

	private int agentCount;
	private final int PORT;
	protected Server theServer;

	public AgentServer(int port, Setup gameSetup) {
		this.PORT = port;
		this.agentCount = 0;
		this.connections = new ConcurrentHashMap<Connection, Integer>();
		this.privateToPublic = new ConcurrentHashMap<Integer, Integer>();
		this.bank = new ConcurrentHashMap<Integer, Account>();
		this.pendingTradeRequests = new CopyOnWriteArrayList<NegotiateRequest>();
		this.auctions = new ConcurrentHashMap<Integer, OneSidedAuction>();
		this.exchange = new Exchange();
		this.privateToPublic.put(-1, -1);

		theServer = new Server();
		theServer.start();
		Kryo serverKryo = theServer.getKryo();
		Startup.start(serverKryo);
		if (gameSetup != null) {
			gameSetup.setup(serverKryo);
		}

		try {
			theServer.bind(PORT, PORT);
		} catch (IOException e) {
			Logging.log("[X] Server failed to start due to port conflict");
			return;
		}

		final AgentServer aServer = this;
		theServer.addListener(new Listener() {
			public void received(Connection connection, Object message) {
				Integer id = null;
				if (connections.containsKey(connection)) {
					id = connections.get(connection);
				} else if (message instanceof Registration) {
					Logging.log("[-] registration recieved from "
							+ connection.getID());
					aServer.onRegistration(connection, (Registration) message);
					return;
				} else {
					return;
				}

				if (message instanceof Bid) {
					Logging.log("[-] bid recieved from " + id);
					aServer.onBid(connection, id, (Bid) message);
				} else if (message instanceof PurchaseRequest) {
					Logging.log("[-] purchaserequest recieved from " + id);
					aServer.onPurchaseRequest(connection, id,
							(PurchaseRequest) message);
				} else if (message instanceof NegotiateRequest) {
					Logging.log("[-] traderequest recieved from " + id);
					aServer.onTradeRequest(connection, id,
							(NegotiateRequest) message);
				} else if (message instanceof Trade) {
					Logging.log("[-] trade recieved from " + id);
					aServer.onTrade(connection, (Trade) message);
				} else if (message instanceof LimitOrder) {
					Logging.log("[-] limitorder recieved from " + id);
					aServer.onLimitOrder(connection, id, (LimitOrder) message);
				}
			}
		});
		Logging.log("[-] server started");
	}

	/**
	 * Deals with securities where you must post prices
	 * 
	 * @param connection
	 *            - details of their connection to the server
	 * @param privateID
	 *            - (safe) privateID of the requesting agent
	 * @param message
	 *            - limit order for a security
	 */
	protected void onLimitOrder(Connection connection, Integer privateID,
			LimitOrder limitorder) {
		TwoSidedAuction market = exchange.getTwoSidedAuction(limitorder.market
				.getID());
		synchronized (market) {
			Ledger ledger = exchange.getLedger(limitorder.market.getID());
			if (market == null) {
				Rejection rej = new Rejection(privateID, limitorder);
				this.theServer.sendToTCP(connection.getID(), rej);
				return;
			}

			if (limitorder.buyShares > 0) {
				synchronized (privateID) {
					Account account = this.bank.get(privateID);
					if (account.monies < market.quoteBid(limitorder.buyShares,
							limitorder.price)) {
						Rejection rej = new Rejection(privateID, limitorder);
						this.theServer.sendToTCP(connection.getID(), rej);
						return;
					}

					List<Order> trans = market.buy(privateID,
							limitorder.buyShares, limitorder.price);
					for (Order t : trans) {
						Tradeable split = null;
						if (t.GOOD.getCount() > t.QUANTITY) {
							split = t.GOOD.split(t.QUANTITY);
							ledger.add(split);
						}

						if (t.FROM != null) {
							synchronized (t.FROM) {
								Account fromBank = this.bank.get(t.FROM);
								if (!fromBank.goods.contains(t.GOOD)) {
									// TODO: Deal with this case
								}
								Account finalUpdatedFrom = fromBank.add(t.COST,
										new HashSet<Tradeable>());
								if (split == null) {
									finalUpdatedFrom = finalUpdatedFrom.remove(
											0, t.GOOD);
								}
								this.bank.put(t.FROM, finalUpdatedFrom);
								this.sendBankUpdate(t.FROM, fromBank,
										finalUpdatedFrom);
							}
						}

						if (t.TO != null) {
							synchronized (t.TO) {
								Account toBank = this.bank.get(t.TO);
								Account oldbank = toBank;
								if (toBank.monies >= t.COST) {
									if (split == null) {
										t.GOOD.setAgentID(t.TO);
										toBank = toBank
												.add(-1 * t.COST, t.GOOD);
									} else {
										split.setAgentID(t.TO);
										toBank = toBank.add(-1 * t.COST, split);
									}
									this.bank.put(t.TO, toBank);
									this.sendBankUpdate(t.TO, oldbank, toBank);
								} else {
									// TODO: Could not afford
								}
							}
						}

						ledger.add(t.GOOD);
					}
				}

			} else if (limitorder.sellShares > 0) {
				synchronized (privateID) {
					Account sellerAccount = this.bank.get(privateID);
					double qToSell = limitorder.sellShares;
					for (Tradeable tradeable : sellerAccount.goods) {
						if (qToSell <= 0) {
							break;
						}

						if (tradeable.getType().equals(market.getType())) {
							Tradeable toSell = tradeable;
							if (tradeable.getCount() > qToSell) {
								toSell = tradeable.split(qToSell);
							}
							qToSell -= toSell.getCount();

							List<Order> trans = market.sell(privateID,
									toSell, limitorder.price);
							for (Order t : trans) {
								if (t.FROM != null) {
									synchronized (t.FROM) {
										Account fromBank = this.bank
												.get(t.FROM);
										if (!fromBank.goods.contains(t.GOOD)) {
											// TODO: Deal with this case
										}
										Account finalUpdatedFrom = fromBank
												.add(t.COST,
														new HashSet<Tradeable>());
										this.bank.put(t.FROM, finalUpdatedFrom);
										this.sendBankUpdate(t.FROM, fromBank,
												finalUpdatedFrom);
									}
								}

								if (t.TO != null) {
									synchronized (t.TO) {
										Account toBank = this.bank.get(t.TO);
										Account oldBank = toBank;
										if (toBank.monies >= t.COST) {
											t.GOOD.setAgentID(t.TO);
											toBank = toBank.add(-1 * t.COST,
													t.GOOD);
											this.bank.put(t.TO, toBank);
											this.sendBankUpdate(t.TO, oldBank,
													toBank);
										} else {
											// TODO: Could not afford
										}
									}
								}

								ledger.add(t.GOOD);
							}
						}
					}
				}
			}
		}
	}

	/*
	 * This method is invoked when a new agent connects to the game
	 * 
	 * @param connection - details of their connection to the server
	 * 
	 * @param registration - details of their game logic agent status
	 */
	protected void onRegistration(Connection connection,
			Registration registration) {
		Integer theID = this.defaultRegistration(connection, registration);
		if (theID == null) {
			// TODO: add rejection
			return;
		}
		theServer.sendToTCP(connection.getID(), new Registration(theID));
	}

	/*
	 * The server receives trade requests and forwards them to the correct
	 * agent(s)
	 * 
	 * @param connection - agent connection info
	 * 
	 * @param privateID - (safe) privateID of the requesting agent
	 * 
	 * @param tradeRequest - the trade request
	 */
	protected void onTradeRequest(Connection connection, Integer privateID,
			NegotiateRequest tradeRequest) {
		NegotiateRequest tr = tradeRequest.safeCopy(privateToPublic
				.get(privateID));
		if (privateID != -1) {
			theServer.sendToAllTCP(tr);
		} else {
			theServer.sendToTCP(connection.getID(), tr);
		}
		pendingTradeRequests.add(tr);
	}

	/*
	 * What happens when a trade request is accepted or rejected
	 * 
	 * @param connection - agent connection info
	 * 
	 * @param trade - tuple of trade request and acceptance boolean
	 */
	protected void onTrade(Connection connection, Trade trade) {
		Integer privateTo = connections.get(connection);
		Integer privateFrom = publicToPrivate(trade.tradeRequest.fromID);
		if (privateFrom == null) {
			return;
		}
		if (pendingTradeRequests.contains(trade.tradeRequest)) {
			if (!trade.accept) {
				if (privateToPublic.get(privateTo) == trade.tradeRequest.fromID
						|| privateToPublic.get(privateTo) == trade.tradeRequest.toID) {
					pendingTradeRequests.remove(trade.tradeRequest);
				}
			} else if (privateToPublic.get(privateTo) == trade.tradeRequest.toID
					|| trade.tradeRequest.toID == -1) {
				Account toAccount = bank.get(privateTo);
				Account fromAccount = bank.get(privateFrom);

				if (trade.tradeRequest.isSatisfied(toAccount, fromAccount)) {
					Account middleTo = toAccount.remove(
							trade.tradeRequest.moniesRequested,
							trade.tradeRequest.sharesRequested);
					Account newTo = middleTo.addAll(
							trade.tradeRequest.moniesOffered,
							trade.tradeRequest.sharesOffered);

					Account middleFrom = fromAccount.remove(
							trade.tradeRequest.moniesOffered,
							trade.tradeRequest.sharesOffered);
					Account newFrom = middleFrom.addAll(
							trade.tradeRequest.moniesRequested,
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

	/*
	 * This will handle the logic for requests to purchase from public markets
	 */
	protected void onPurchaseRequest(Connection connection, Integer privateID,
			PurchaseRequest purchaseRequest) {
		TwoSidedAuction market = exchange
				.getTwoSidedAuction(purchaseRequest.market.getID());
		Ledger ledger = exchange.getLedger(purchaseRequest.market.getID());
		Account oldAccount = bank.get(privateID);
		synchronized (market) {
			synchronized (oldAccount) {
				if (market == null) {
					Rejection rej = new Rejection(privateID, purchaseRequest);
					this.theServer.sendToTCP(connection.getID(), rej);
					return;
				}

				/*
				 * double cost = market.cost(purchaseRequest.buyQuantity,
				 * purchaseRequest.sellQuantity); if (oldAccount.monies >= cost)
				 * { //TODO: Update so that purchases can affect multiple agents
				 * //using multiple transactions; all safely locked etc
				 * List<Tradeable> update = new LinkedList<Tradeable>(); if
				 * (purchaseRequest.buyQuantity > 0) { List<Transaction> yes =
				 * market.buy(privateID, purchaseRequest.buyQuantity, -1);
				 * update.addAll(yes); ledger.add(yes); }
				 * 
				 * if (purchaseRequest.sellQuantity > 0) { List<Transaction> no
				 * = market.sell(privateID, purchaseRequest.sellQuantity, -1);
				 * update.addAll(no); ledger.add(no); }
				 * 
				 * Account newAccount = oldAccount.addAll(0, update); newAccount
				 * = newAccount.remove(cost, null); bank.put(privateID,
				 * newAccount); BankUpdate bu = new BankUpdate(privateID,
				 * oldAccount, newAccount);
				 * theServer.sendToTCP(connection.getID(), bu);
				 * //this.sendMarketUpdate(market); } else { Rejection rej = new
				 * Rejection(privateID, purchaseRequest);
				 * theServer.sendToTCP(connection.getID(), rej); }
				 */
			}
		}
	}

	/*
	 * This will handle what happens when an agent sends in a bid in response to
	 * a BidRequest for an auction
	 */
	protected void onBid(Connection connection, Integer privateID, Bid bid) {
		OneSidedAuction auction = this.auctions.get(bid.AuctionID);
		if (auction != null) {
			synchronized (auction) {
				Account account = this.bank.get(privateID);
				if (account.monies >= bid.Bundle.getCost()) {
					auction.handleBid(bid.safeCopy(privateID));
				} else {
					Rejection rej = new Rejection(privateID, bid);
					this.theServer.sendToTCP(connection.getID(), rej);
				}
			}
		}
	}

	/*
	 * Sends a bank update to a set of agents
	 * 
	 * @param List<Integer> set of IDs to send to
	 */
	public void sendBankUpdates(List<Integer> IDs) {
		synchronized (IDs) {
			for (Integer ID : IDs) {
				Connection connection = privateToConnection(ID);
				if (connection == null) {
					continue;
				}
				Account account = this.bank.get(ID);
				if (account == null) {
					continue;
				}
				BankUpdate bu = new BankUpdate(ID, null, account.toAgent());
				theServer.sendToTCP(connection.getID(), bu);
			}
		}
	}

	/*
	 * Sends a market update to every agent about the state of all the public
	 * markets
	 * 
	 * NOTE: No need for sync since this is access only
	 */
	public void sendAllMarketUpdates(List<TwoSidedAuction> tsas) {
		int i = 0;
		for (TwoSidedAuction sec : tsas) {
			MarketUpdate mupdate = new MarketUpdate(i++, sec.wrap(),
					sec.getMechanismType());
			theServer.sendToAllTCP(mupdate);
		}
	}

	/*
	 * Sends a auction update to every agent or closes out any finished
	 * auctions. about the state of all the public auctions
	 */
	public void updateAllAuctions() {
		synchronized (auctions) {
			List<OneSidedAuction> toRemove = new LinkedList<OneSidedAuction>();
			for (OneSidedAuction auction : auctions.values()) {
				synchronized (auction) {
					auction.tick(System.currentTimeMillis());
					if (auction.isClosed()) {
						toRemove.add(auction);
						Map<BidBundle, Set<Tradeable>> winners = auction
								.getWinners();
						if (winners == null) {
							continue;
						}
						for (BidBundle winner : winners.keySet()) {
							Account account = this.bank.get(winner.getAgent());
							if (account == null) {
								continue;
							}
							// TODO: Think about locking w/ account object
							// changes
							synchronized (account) {
								for (Tradeable t : winners.get(winner)) {
									t.setAgentID(winner.getAgent());
								}
								Account newA = account.add(
										-1 * winner.getCost(),
										winners.get(winner));
								this.bank.put(winner.getAgent(), newA);
								this.sendBankUpdate(winner.getAgent(), account,
										newA);
							}
						}
					} else {
						for (Map.Entry<Connection, Integer> id : this.connections
								.entrySet()) {
							TradeRequest br = auction.getBidRequest(id
									.getValue());
							if (br == null) {
								continue;
							}
							this.theServer.sendToTCP(id.getKey().getID(), br);
						}
					}
				}
			}

			for (OneSidedAuction auction : toRemove) {
				auctions.remove(auction);
			}
		}
	}

	/*
	 * Sends a MarketUpdate about this specific market to all agents
	 * 
	 * @param Security : the market to update on
	 */
	public void sendMarketUpdate(TwoSidedAuction market) {
		MarketUpdate mupdate = new MarketUpdate(0, market.wrap(),
				market.getMechanismType());
		theServer.sendToAllTCP(mupdate);
	}

	/*
	 * Agents only know each other's public IDs. Private IDs are only known to
	 * the agents themselves and are needed to authorize any actions. The server
	 * refers to agents by their private IDs at all times.
	 */
	protected Integer publicToPrivate(Integer id) {
		for (Entry<Integer, Integer> ptp : privateToPublic.entrySet()) {
			if (ptp.getValue() == id) {
				return ptp.getKey();
			}
		}

		return null;
	}

	/*
	 * Retrieves a connection (needed to send a message to a client) from the
	 * agent's private ID
	 */
	protected Connection privateToConnection(Integer id) {
		if (id == null) {
			return null;
		}

		for (Entry<Connection, Integer> ctp : connections.entrySet()) {
			if (ctp.getValue().intValue() == id.intValue()) {
				return ctp.getKey();
			}
		}

		return null;
	}

	/*
	 * Retrieves an agent's bank account from its public ID
	 */
	public Account publicToAccount(Integer id) {
		return bank.get(publicToPrivate(id));
	}

	/*
	 * Retrieves an agent's bank account from its private ID
	 */
	public Account privateToAccount(Integer id) {
		return bank.get(id);
	}

	/*
	 * Sets an agent's bank account from its private ID
	 */
	public void setAccount(Integer id, Account account) {
		synchronized (id) {
			bank.put(id, account);
		}
	}

	/*
	 * Sends a bank update to a set of agents
	 * 
	 * @param List<Integer> set of IDs to send to
	 */
	public void sendBankUpdates(Set<Integer> IDs) {
		synchronized (IDs) {
			for (Integer ID : IDs) {
				Connection connection = privateToConnection(ID);
				if (connection == null) {
					continue;
				}
				BankUpdate bu = new BankUpdate(ID, null, bank.get(ID));
				theServer.sendToTCP(connection.getID(), bu);
			}
		}
	}

	/**
	 * Singular bank update
	 * 
	 * @param ID
	 * @param oldA
	 * @param newA
	 */
	public void sendBankUpdate(Integer ID, Account oldA, Account newA) {
		BankUpdate bu = new BankUpdate(ID, oldA.toAgent(), newA.toAgent());
		theServer.sendToTCP(this.privateToConnection(ID).getID(), bu);
	}

	/**
	 * Default registration; allows modified reg message
	 * @param connection : new connection
	 * @param registration : new registration
	 * @return safe privateID mapped to connection
	 */
	public Integer defaultRegistration(Connection connection,
			Registration registration) {
		if (registration.getID() == null) {
			return null;
		}

		Collection<Integer> allIds = connections.values();
		Integer theID = registration.getID();
		if (allIds.contains(theID)) {
			Connection oldConnection = null;
			for (Connection c : connections.keySet()) {
				if (connections.get(c).equals(theID)) {
					oldConnection = c;
					if (!oldConnection.getRemoteAddressTCP().equals(
							connection.getRemoteAddressTCP())) {
						return null;
					}
					break;
				}
			}
			connections.remove(oldConnection);

			return null;
		} else {
			theID = new Integer((int) (Math.random() * 1000000000));
			while (allIds.contains(theID)) {
				theID = new Integer((int) (Math.random() * 1000000000));
			}

			privateToPublic.put(theID, agentCount++);
			bank.put(theID, new Account(theID));

			connections.put(connection, theID);
			Logging.log("[-] registered " + theID);

			return theID;
		}
	}

}
