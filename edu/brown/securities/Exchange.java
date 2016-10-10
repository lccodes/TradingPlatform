package brown.securities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import brown.assets.accounting.Ledger;
import brown.server.AgentServer;

public class Exchange {
	private Map<Security, Ledger> ledgers;
	private Map<Integer, Security> securities;
	
	public Exchange() {
		this.ledgers = new ConcurrentHashMap<Security, Ledger>();
		this.securities = new ConcurrentHashMap<Integer, Security>();
	}
	
	public void close(AgentServer server, Security security, boolean pay) {
		synchronized(security){
			ledgers.get(security).close(server, security, pay);
			ledgers.remove(security);
			securities.remove(security.getID());
		}
	}
	
	public void close(AgentServer server, Integer ID, boolean pay) {
		Security security = securities.get(ID);
		synchronized(security){
			ledgers.get(security).close(server, security, pay);
			ledgers.remove(security);
			securities.remove(security.getID());
		}
	}
	
	public boolean open(Security security, Ledger ledger) {
		if (ledgers.containsKey(security)) {
			return false;
		}
		this.ledgers.put(security, ledger);
		this.securities.put(security.getID(), security);
		
		return true;
	}
	
	public Ledger getLedger(Integer ID) {
		return ledgers.get(securities.get(ID));
	}
	
	public Security getSecurity(Integer ID) {
		return securities.get(ID);
	}

}
