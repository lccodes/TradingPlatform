package brown.agent.library;

import brown.assets.accounting.Transaction;
import brown.exceptions.AgentCreationException;
import brown.markets.ContinuousDoubleAuction;
import brown.markets.SimpleAuction;
import brown.markets.UnitCDA;
import brown.messages.markets.GameReport;
import brown.setup.Logging;

public class LukeAgent extends Lab4Agent {
	private boolean once = false;

	public LukeAgent(String host, int port) throws AgentCreationException {
		super(host, port);
	}

	@Override
	public void onContinuousDoubleAuction(ContinuousDoubleAuction market) {
		//System.out.println("MY DECOYS " + this.myNumDecoys);
		for (Transaction t : market.getLedger().getList()) {
			Logging.log("Tradeable " + t.TRADEABLE + " was sold to " + t.TO + " from " + t.FROM + " at price " + t.PRICE);
		}
		if (Math.random() > .5 && !this.once) {
			market.buy(this, 1, 10);
			this.once = true;
		} else if (!this.once){
			market.sell(this, 1, 10);
			this.once = true;
		}
	}

	@Override
	public void onMarketUpdate(GameReport marketUpdate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onContinuousDoubleAuction(UnitCDA unitCDAWrapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSimpleSealed(SimpleAuction simpleWrapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSimpleOpenOutcry(SimpleAuction simpleWrapper) {
		// TODO Auto-generated method stub
		
	}

}
