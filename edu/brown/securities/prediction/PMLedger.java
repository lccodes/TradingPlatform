package brown.securities.prediction;

import java.util.HashSet;
import java.util.Set;

import brown.assets.accounting.Account;
import brown.assets.accounting.Ledger;
import brown.assets.accounting.Transaction;
import brown.securities.Security;
import brown.server.AgentServer;

public class PMLedger extends Ledger {

	@Override
	public void close(AgentServer server, Security security, boolean pay) {
		if (!pay) {
			return;
		}
		
		Set<Integer> ids = new HashSet<Integer>();
		for (Transaction t : this.transactions) {
			Account account = server.publicToAccount(t.getAgentID());
			synchronized(account) {
				Account newAccount = account.add(t.getCount(), null);
				server.setAccount(t.getAgentID(), newAccount);
			}
			ids.add(account.ID);
		}
		
		server.sendBankUpdates(ids);	
	}

}
