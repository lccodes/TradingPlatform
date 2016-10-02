package brown.securities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import brown.assets.accounting.Ledger;
import brown.server.AgentServer;

public abstract class Exchange {
	private Map<Security, Ledger> securities;
	
	public Exchange() {
		this.securities = new ConcurrentHashMap<Security, Ledger>();
	}
	
	public void close(AgentServer server, Security security, boolean pay) {
		synchronized(security){
			securities.get(security).close(server, security, pay);
			securities.remove(security);
		}
	}
	
	public void open(Security security, Ledger ledger) {
		if (securities.containsKey(security)) {
			return;
		}
		this.securities.put(security, ledger);
	}

}
