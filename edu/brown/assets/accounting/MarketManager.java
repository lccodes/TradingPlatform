package brown.assets.accounting;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import brown.assets.value.ITradeable;
import brown.assets.value.StateOfTheWorld;
import brown.auctions.IMarket;
import brown.server.AgentServer;
import brown.setup.Logging;

public class MarketManager {
	private Map<IMarket, Ledger> ledgers;
	private Map<Integer, IMarket> tsauctions;

	public MarketManager() {
		this.ledgers = new ConcurrentHashMap<IMarket, Ledger>();
		this.tsauctions = new ConcurrentHashMap<Integer, IMarket>();
	}

	/**
	 * Closes a market TODO: Close pair markets together i.e. PMs
	 * 
	 * @param server
	 * @param market
	 * @param closingState
	 */
	public void convert(AgentServer server, IMarket market,
			StateOfTheWorld closingState) {
		synchronized (market) {
			Ledger ledger = this.ledgers.get(market);
			synchronized (ledger) {
				for (Transaction t : ledger.getLatest()) {
					Account toReplace = t.TRADEABLE.convert(closingState);
					synchronized (t.TRADEABLE.getAgentID()) {
						Account oldAccount = server.privateToAccount(t
								.TRADEABLE.getAgentID());
						if (oldAccount == null) {
							Logging.log("[X] agent without account "
									+ t.TRADEABLE.getAgentID());
							continue;
						}

						Account newAccount = oldAccount.remove(0, t.TRADEABLE);
						server.setAccount(t.TRADEABLE.getAgentID(), newAccount);
						if (toReplace == null) {
							server.sendBankUpdate(t.TRADEABLE.getAgentID(), oldAccount,
									newAccount);
						}
					}

					if (toReplace != null) {
						Integer toReplaceID = toReplace.ID;
						if (toReplaceID == null) {
							toReplaceID = t.TRADEABLE.getAgentID();
						}

						synchronized (toReplaceID) {
							Account oldAccount = server
									.privateToAccount(toReplaceID);
							if (oldAccount == null) {
								Logging.log("[X] agent without account "
										+ toReplaceID);
								continue;
							}

							Account newAccount = oldAccount.add(
									toReplace.monies, new HashSet<ITradeable>(
											toReplace.tradeables));
							server.setAccount(toReplaceID, newAccount);
							server.sendBankUpdate(toReplaceID, oldAccount,
									newAccount);
						}
					}
				}

				ledgers.remove(market);
				tsauctions.remove(market.getID());
			}
		}
	}

	/**
	 * Closes a market and tells it to convert if applicable
	 * @param server
	 * @param ID
	 * @param closingState
	 */
	public void close(AgentServer server, Integer ID, StateOfTheWorld closingState) {
		IMarket market = tsauctions.get(ID);
		//TODO market.close()
		convert(server, market, closingState);
	}

	/**
	 * Opens a market
	 * @param market
	 * @return
	 */
	public boolean open(IMarket market) {
		if (ledgers.containsKey(market)) {
			return false;
		}
		this.ledgers.put(market, new Ledger(market));
		this.tsauctions.put(market.getID(), market);

		return true;
	}
	
	public boolean register(Integer ID, ITradeable t) {
		IMarket tsa = tsauctions.get(ID);
		if (tsa == null) {
			return false;
		}
		synchronized(tsa) {
			//if (!tsa.getType().equals(t.getType())) {
			//	return false;
			//}
			
			Ledger ledger = this.ledgers.get(tsa);
			synchronized (ledger) {
				ledger.add(new Transaction(t.getAgentID(), null, 0,0,t));
			}
		}
	
		return true;
	}

	public Ledger getLedger(Integer ID) {
		return ledgers.get(tsauctions.get(ID));
	}

	public IMarket getIMarket(Integer ID) {
		return tsauctions.get(ID);
	}

	public Collection<IMarket> getAuctions() {
		return this.tsauctions.values();
	}

}
