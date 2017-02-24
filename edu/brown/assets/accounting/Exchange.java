package brown.assets.accounting;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import brown.assets.value.State;
import brown.assets.value.Tradeable;
import brown.auctions.twosided.TwoSidedAuction;
import brown.server.AgentServer;
import brown.setup.Logging;

public class Exchange {
	private Map<TwoSidedAuction, Ledger> ledgers;
	private Map<Integer, TwoSidedAuction> tsauctions;
	
	public Exchange() {
		this.ledgers = new ConcurrentHashMap<TwoSidedAuction, Ledger>();
		this.tsauctions = new ConcurrentHashMap<Integer, TwoSidedAuction>();
	}
	
	/**
	 * Closes a market
	 * TODO: Close pair markets together i.e. PMs
	 * @param server
	 * @param tsa
	 * @param closingState
	 */
	public void close(AgentServer server, TwoSidedAuction tsa, State closingState) {
		synchronized(tsa){
			for (Tradeable t : this.ledgers.get(tsa).getSet()) {
				Account toReplace = t.close(closingState);
				synchronized(t.getAgentID()) {
					Account oldAccount = server.privateToAccount(t.getAgentID());
					if (oldAccount == null) {
						Logging.log("[X] agent without account " + t.getAgentID());
						continue;
					}
					
					Account newAccount = oldAccount.remove(0, t);
					server.setAccount(t.getAgentID(), newAccount);
					if (toReplace == null || (toReplace.ID != null && !toReplace.ID.equals(t.getAgentID()))) {
						server.sendBankUpdate(t.getAgentID(), oldAccount, newAccount);
					}
				}
				
				if (toReplace != null) {
					Integer toReplaceID = toReplace.ID;
					if (toReplaceID == null) {
						toReplaceID = t.getAgentID();
					}
					
					synchronized(toReplaceID) {
						Account oldAccount = server.privateToAccount(toReplaceID);
						if (oldAccount == null) {
							Logging.log("[X] agent without account " + toReplaceID);
							continue;
						}
						
						Account newAccount = oldAccount.add(toReplace.monies, new HashSet<Tradeable>(toReplace.goods));
						server.setAccount(toReplaceID, newAccount);
						server.sendBankUpdate(toReplaceID, oldAccount, newAccount);
					}
				}
			}
			
			
			ledgers.remove(tsa);
			tsauctions.remove(tsa.getID());
		}
	}
	
	public void close(AgentServer server, Integer ID, State closingState) {
		TwoSidedAuction tsa = tsauctions.get(ID);
		close(server, tsa, closingState);
	}
	
	public boolean open(TwoSidedAuction tsa) {
		if (ledgers.containsKey(tsa)) {
			return false;
		}
		this.ledgers.put(tsa, new Ledger(tsa));
		this.tsauctions.put(tsa.getID(), tsa);
		
		return true;
	}
	
	public Ledger getLedger(Integer ID) {
		return ledgers.get(tsauctions.get(ID));
	}
	
	public TwoSidedAuction getTwoSidedAuction(Integer ID) {
		return tsauctions.get(ID);
	}
	
	public Collection<TwoSidedAuction> getAuctions() {
		return this.tsauctions.values();
	}

}
