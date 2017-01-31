package brown.securities.prediction;

import brown.assets.accounting.Ledger;
import brown.securities.Security;
import brown.server.AgentServer;

public class PMLedger extends Ledger {
	private boolean closed;
	private PMLedger other;
	
	public PMLedger(Security security, PMLedger other) {
		super(security);
		this.closed = false;
		this.other = other;
	}

	@Override
	public void close(AgentServer server, boolean pay) {
		if (closed) {
			return;
		}
		
		if (!pay) {
			this.closed = true;
			if (other != null) {
				other.close(server, pay);
			}
			
			return;
		}
		
		server.sendBankUpdates(this.pay(server));
		if (other != null) {
			other.close(server, pay);
		}
	}
	
	public void setLedger(PMLedger ledger) {
		this.other = ledger;
	}

}
