package brown.securities.cda;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Ledger;
import brown.assets.accounting.Transaction;
import brown.securities.Security;
import brown.server.AgentServer;

public class CDALedger extends Ledger {
	protected List<Transaction> pendingTransactions;
	protected boolean closed;

	public CDALedger(Security security) {
		super(security);
		this.pendingTransactions = new LinkedList<Transaction>();
		this.closed = false;
	}

	@Override
	public void close(AgentServer server, boolean pay) {
		if (closed) {
			return;
		}
		
		if (!pay) {
			this.closed = true;			
			return;
		}
		
		server.sendBankUpdates(this.pay(server));
	}

}
