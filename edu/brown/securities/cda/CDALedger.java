package brown.securities.cda;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Ledger;
import brown.assets.accounting.TransactionOld;
import brown.securities.SecurityOld;
import brown.server.AgentServer;

public class CDALedger extends Ledger {
	protected List<TransactionOld> pendingTransactions;
	protected boolean closed;

	public CDALedger(SecurityOld security) {
		super(security);
		this.pendingTransactions = new LinkedList<TransactionOld>();
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
