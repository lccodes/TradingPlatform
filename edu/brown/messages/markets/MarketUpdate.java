package brown.messages.markets;

import brown.agent.Agent;
import brown.assets.accounting.Ledger;
import brown.messages.Message;

public class MarketUpdate extends Message {
	public final Ledger LEDGER;
	
	public MarketUpdate() {
		super(null);
		this.LEDGER = null;
	}

	public MarketUpdate(Ledger ledger) {
		super(null);
		this.LEDGER = ledger;
	}

	@Override
	public void dispatch(Agent agent) {
		agent.onMarketUpdate(this);
	}

}
