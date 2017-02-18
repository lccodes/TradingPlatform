package brown.assets.accounting;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import brown.assets.value.Tradeable;
import brown.auctions.TwoSidedAuction;
import brown.server.AgentServer;
import brown.setup.Logging;

public class Exchange {
	private Map<TwoSidedAuction, Ledger> ledgers;
	private Map<Integer, TwoSidedAuction> tsauctions;
	
	public Exchange() {
		this.ledgers = new ConcurrentHashMap<TwoSidedAuction, Ledger>();
		this.tsauctions = new ConcurrentHashMap<Integer, TwoSidedAuction>();
	}
	
	public void close(AgentServer server, TwoSidedAuction tsa) {
		synchronized(tsa){
			for (Tradeable t : this.ledgers.get(tsa).getList()) {
				Account toReplace = tsa.close(t);
				if (toReplace == null || toReplace.ID == null) {
					continue;
				}
				
				synchronized(t.getAgentID()) {
					Account oldAccount = server.publicToAccount(t.getAgentID());
					if (oldAccount == null) {
						Logging.log("[X] agent without account " + t.getAgentID());
						continue;
					}
					server.setAccount(t.getAgentID(), oldAccount.remove(0, t));
				}
				
				synchronized(toReplace.ID) {
					Account oldAccount = server.publicToAccount(toReplace.ID);
					if (oldAccount == null) {
						Logging.log("[X] agent without account " + toReplace.ID);
						continue;
					}
					server.setAccount(toReplace.ID, oldAccount.add(toReplace.monies, new HashSet<Tradeable>(toReplace.goods)));
				}
				
				//TODO: Where to update?
			}
			
			
			ledgers.remove(tsa);
			tsauctions.remove(tsa.getID());
		}
	}
	
	public void close(AgentServer server, Integer ID) {
		TwoSidedAuction tsa = tsauctions.get(ID);
		close(server, tsa);
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

}
