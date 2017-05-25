package brown.assets.accounting;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import brown.assets.value.Tradeable;
import brown.assets.value.StateOfTheWorld;
import brown.auctions.interfaces.Market;
import brown.auctions.twosided.TwoSidedAuction;
import brown.server.AgentServer;
import brown.setup.Logging;

public class MarketManager {
	private Map<Market, Ledger> ledgers;
	private Map<Integer, Market> tsauctions;
	
	private Map<Integer, TwoSidedAuction> twosided;

	public MarketManager() {
		this.ledgers = new ConcurrentHashMap<Market, Ledger>();
		this.tsauctions = new ConcurrentHashMap<Integer, Market>();
		
		this.twosided = new ConcurrentHashMap<Integer,TwoSidedAuction>();
	}
	
	/**
	 * Process each account
	 * @param server
	 * @param market
	 * @param ledger
	 * @param t
	 * @param toReplace
	 */
	private void process(AgentServer server, Market market, Ledger ledger, 
			Transaction t, Account toReplace) {
		synchronized (t.TRADEABLE.getAgentID()) {
			Account oldAccount = server.privateToAccount(t
					.TRADEABLE.getAgentID());
			if (oldAccount == null) {
				Logging.log("[X] agent without account "
						+ t.TRADEABLE.getAgentID());
				return;
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
					return;
				}

				Account newAccount = oldAccount.add(
						toReplace.monies, new HashSet<Tradeable>(
								toReplace.tradeables));
				server.setAccount(toReplaceID, newAccount);
				server.sendBankUpdate(toReplaceID, oldAccount,
						newAccount);
			}
		}
	}

	/**
	 * Closes a market TODO: Close pair markets together i.e. PMs
	 * 
	 * @param server
	 * @param market
	 * @param closingState
	 */
	public void convert(AgentServer server, Market market,
			StateOfTheWorld closingState) {
		synchronized (market) {
			Ledger ledger = this.ledgers.get(market);
			synchronized (ledger) {
				for (Transaction t : ledger.getLatest()) {
					List<Account> allReplacements = t.TRADEABLE.convert(closingState);
					if (allReplacements != null) {
						for (Account toReplace : allReplacements) {
							this.process(server, market, ledger, t, toReplace);
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
		Market market = tsauctions.get(ID);
		//TODO market.close()
		convert(server, market, closingState);
	}

	/**
	 * Opens a market
	 * @param market
	 * @return
	 */
	public boolean open(Market market) {
		if (ledgers.containsKey(market)) {
			return false;
		}
		this.ledgers.put(market, new Ledger(market));
		this.tsauctions.put(market.getID(), market);

		return true;
	}
	
	/**
	 * Registers a security created outside the market
	 * @param ID
	 * @param t
	 * @return
	 */
	public boolean register(Integer ID, Tradeable t) {
		Market tsa = tsauctions.get(ID);
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

	/**
	 * Gets the ledger for this market ID
	 * @param ID
	 * @return
	 */
	public Ledger getLedger(Integer ID) {
		return ledgers.get(tsauctions.get(ID));
	}

	/**
	 * Gets the market for this ID
	 * @param ID
	 * @return
	 */
	public Market getIMarket(Integer ID) {
		return tsauctions.get(ID);
	}

	/**
	 * Gets all of the auctions
	 * @return
	 */
	public Collection<Market> getAuctions() {
		return this.tsauctions.values();
	}

	public TwoSidedAuction getTwoSided(Integer marketID) {
		return this.twosided.get(marketID);
	}
	
	public void openTwoSided(TwoSidedAuction tsa) {
		this.twosided.put(tsa.getID(), tsa);
	}

}
