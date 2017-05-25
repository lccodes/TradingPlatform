package brown.messages.markets;

import brown.agent.Agent;
import brown.assets.accounting.Ledger;
import brown.messages.Message;

public class GameReport extends Message {
	public final Ledger LEDGER;
	
	public GameReport() {
		super(null);
		this.LEDGER = null;
	}

	public GameReport(Ledger ledger) {
		super(null);
		this.LEDGER = ledger;
	}

	@Override
	public void dispatch(Agent agent) {
		agent.onMarketUpdate(this);
	}

}
