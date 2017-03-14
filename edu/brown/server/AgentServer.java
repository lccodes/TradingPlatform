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
import brown.assets.accounting.Ledger;
import brown.assets.accounting.MarketManager;
import brown.assets.accounting.Order;
import brown.assets.accounting.Transaction;
import brown.assets.value.ITradeable;
import brown.auctions.IMarket;
import brown.auctions.bundles.BidBundle;
import brown.auctions.crules.ShortShare;
import brown.auctions.onesided.OneSidedAuction;
import brown.auctions.twosided.TwoSidedAuction;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.auctions.Bid;
import brown.messages.markets.GameReport;
import brown.messages.markets.MarketOrder;
import brown.messages.markets.TradeRequest;
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
	protected MarketManager manager;

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
		this.manager = new MarketManager();
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
				} else if (message instanceof NegotiateRequest) {
					Logging.log("[-] traderequest recieved from " + id);
					aServer.onTradeRequest(connection, id,
							(NegotiateRequest) message);
				} else if (message instanceof Trade) {
					Logging.log("[-] trade recieved from " + id);
					aServer.onTrade(connection, (Trade) message);
				} else if (message instanceof MarketOrder) {
					Logging.log("[-] limitorder recieved from " + id);
					aServer.onLimitOrder(connection, id, (MarketOrder) message);
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
			MarketOrder limitorder) {
		if (limitorder.marketID == null) {
			Ack rej = new Ack(privateID, limitorder, true);
			this.theServer.sendToTCP(connection.getID(), rej);
			return;
		}
		TwoSidedAuction market = (TwoSidedAuction) manager
				.getIMarket(limitorder.marketID);
		synchronized (market) {
			if (market == null) {
				Ack rej = new Ack(privateID, limitorder, true);
				this.theServer.sendToTCP(connection.getID(), rej);
				return;
			}
			Ledger ledger = manager.getLedger(limitorder.marketID);

			if (limitorder.buyShares > 0) {
				synchronized (privateID) {
					Account account = this.bank.get(privateID);
					if (!market.permitShort() && account.monies < market.quoteBid(limitorder.buyShares,
							limitorder.price)) {
						Ack rej = new Ack(privateID, limitorder, true);
						this.theServer.sendToTCP(connection.getID(), rej);
						return;
					}

					List<Order> trans = market.buy(privateID,
							limitorder.buyShares, limitorder.price);
					for (Order t : trans) {
						ITradeable split = null;
						if (t.GOOD.getCount() > t.QUANTITY) {
							split = t.GOOD.split(t.QUANTITY);
							ledger.add(t.toTransaction());
						}

						if (t.FROM != null) {
							synchronized (t.FROM) {
								Account fromBank = this.bank.get(t.FROM);
								if (!market.permitShort() && !fromBank.tradeables.contains(t.GOOD)) {
									// TODO: Deal with this case
								}
								Account finalUpdatedFrom = fromBank.add(t.COST,
										new HashSet<ITradeable>());
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
								if (market.permitShort() || toBank.monies >= t.COST) {
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

						ledger.add(t.toTransaction());
					}
				}
			} else if (limitorder.sellShares > 0) {
				synchronized (privateID) {
					Account sellerAccount = this.bank.get(privateID);
					double qToSell = limitorder.sellShares;
					synchronized (sellerAccount.tradeables) {
						List<ITradeable> justAList = new LinkedList<ITradeable>(sellerAccount.tradeables);
						//Short sale check
						if (market.permitShort()) {
							double toShort = limitorder.sellShares;
							for (ITradeable t : justAList) {
								if (t.getType().equals(market.getTradeableType())) {
									toShort -= t.getCount();
								}
							}
							if (toShort > 0) {
								justAList.add(new ShortShare(toShort, market.getTradeableType()));
							}
						}
						
						for (ITradeable tradeable : justAList) {
							if (qToSell <= 0) {
								break;
							}

							if (tradeable.getType().equals(market.getTradeableType())) {
								ITradeable toSell = tradeable;
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
											if (market.permitShort() || fromBank.tradeables.contains(t.GOOD)) {
												Account taken = fromBank
														.remove(0, t.GOOD);
												Account finalUpdatedFrom = taken
														.add(t.COST,
																new HashSet<ITradeable>());
												this.bank.put(t.FROM,
														finalUpdatedFrom);
												this.sendBankUpdate(t.FROM,
														fromBank,
														finalUpdatedFrom);
											} else {
												// TODO: Deal with this case
											}
										}
									}

									if (t.TO != null) {
										synchronized (t.TO) {
											Account toBank = this.bank
													.get(t.TO);
											Account oldBank = toBank;
											if (market.permitShort() || toBank.monies >= t.COST) {
												t.GOOD.setAgentID(t.TO);
												toBank = toBank.add(
														-1 * t.COST, t.GOOD);
												this.bank.put(t.TO, toBank);
												this.sendBankUpdate(t.TO,
														oldBank, toBank);
											} else {
												// TODO: Could not afford
											}
										}
									}

									ledger.add(t.toTransaction());
								}
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
	 * This will handle what happens when an agent sends in a bid in response to
	 * a BidRequest for an auction
	 */
	protected void onBid(Connection connection, Integer privateID, Bid bid) {
		OneSidedAuction auction = this.manager.getOneSided(bid.AuctionID);
		if (auction != null) {
			synchronized (auction) {
				Account account = this.bank.get(privateID);
				if (auction.permitShort() || account.monies >= bid.Bundle.getCost()) {
					auction.handleBid(bid.safeCopy(privateID));
				} else {
					Ack rej = new Ack(privateID, bid, true);
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
			TradeRequest mupdate = new TradeRequest(i++, sec.wrap(this.manager.getLedger(sec.getID()).getSanitized(null)),
					sec.getMechanismType());
			theServer.sendToAllTCP(mupdate);
		}
	}

	/**
	 * Sends a auction update to every agent or closes out any finished
	 * auctions. about the state of all the public auctions
	 */
	public void updateAllAuctions() {
		synchronized (this.manager) {
			List<OneSidedAuction> toRemove = new LinkedList<OneSidedAuction>();
			for (OneSidedAuction auction : this.manager.getOneSidedAuctions()) {
				synchronized (auction) {
					auction.tick(System.currentTimeMillis());
					if (auction.isClosed()) {
						toRemove.add(auction);
						Map<BidBundle, Set<ITradeable>> winners = auction
								.getWinners();
						if (winners == null) {
							continue;
						}
						Ledger ledger = this.manager.getLedger(auction.getID());
						for (BidBundle winner : winners.keySet()) {
							Account account = this.bank.get(winner.getAgent());
							if (account == null) {
								continue;
							}
							synchronized (account.ID) {
								for (ITradeable t : winners.get(winner)) {
									t.setAgentID(winner.getAgent());
									ledger.add(new Transaction(null, winner.getAgent(), winner.getCost(), t.getCount(), t));
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
							TradeRequest tr = auction.wrap(id.getValue(), 
									this.manager.getLedger(auction.getID()).getSanitized(id.getValue()));
							if (tr == null) {
								continue;
							}
							this.theServer.sendToTCP(id.getKey().getID(), tr);
						}
					}
				}
			}

			for (OneSidedAuction auction : toRemove) {
				GameReport report = auction.getReport();
				if (report != null) {
					this.theServer.sendToAllTCP(report);
				}
				this.manager.close(this, auction.getID(), null);
			}
		}
	}

	/*
	 * Sends a MarketUpdate about this specific market to all agents
	 * 
	 * @param Security : the market to update on
	 */
	public void sendMarketUpdate(IMarket market) {
		TradeRequest mupdate = new TradeRequest(0, market.wrap(this.manager.getLedger(market.getID()).getSanitized(null)),
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
	 * 
	 * @param connection
	 *            : new connection
	 * @param registration
	 *            : new registration
	 * @return safe privateID mapped to connection
	 */
	public Integer defaultRegistration(Connection connection,
			Registration registration) {
		if (registration.getID() == null) {
			return null;
		}
		
		this.theServer.sendToTCP(connection.getID(), new Ack(registration, false));

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
