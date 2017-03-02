package brown.auctions.crules;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Account;
import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.assets.value.Security;
import brown.assets.value.SecurityType;
import brown.assets.value.Tradeable;
import brown.auctions.rules.ClearingRule;
import brown.securities.mechanisms.lmsr.LMSRBackend;

public class LMSRYesClearing implements ClearingRule {
	private final LMSRBackend BACKEND;
	private final FullType TYPE;
	private final boolean SHORT;
	
	public LMSRYesClearing() {
		this.BACKEND = null;
		this.TYPE = null;
		this.SHORT = true;
	}
	
	public LMSRYesClearing(LMSRBackend backend, boolean shortSelling) {
		this.BACKEND = backend;
		this.SHORT = shortSelling;
		this.TYPE = new FullType(SecurityType.PredictionYes, backend.getId());
	}
	
	@Override
	public List<Order> buy(Integer agentID, double shareNum, double sharePrice) {
		double cost = this.BACKEND.bid(shareNum);
		this.BACKEND.yes(null, shareNum);
		List<Order> trans = new LinkedList<Order>();
		Security newSec = new Security(agentID, shareNum, this.TYPE,
				state -> state.getState() == 1 ? new Account(null).add(1) : null);
		newSec.setClosure(state -> state.getState() == 1 ? new Account(null).add(newSec.getCount()) : null);
		trans.add(new Order(agentID, null, cost, shareNum, newSec));
		return trans;
	}

	@Override
	public List<Order> sell(Integer agentID, Tradeable opp, double sharePrice) {
		List<Order> trans = new LinkedList<Order>();
		if (this.SHORT && opp.getAgentID() == null) {
			Security newSec = new Security(agentID, opp.getCount(), this.TYPE,
					state -> state.getState() == 1 ? new Account(null).add(1) : null);
			newSec.setClosure(state -> state.getState() == 1 ? new Account(null).add(newSec.getCount()) : null);
			opp = newSec;
		} else if (opp.getAgentID() == null) {
			return trans;
		}
		double cost = this.BACKEND.bid(-1 * opp.getCount());
		this.BACKEND.yes(null, -1 *opp.getCount());
		trans.add(new Order(null, agentID, cost, opp.getCount(), opp));
		return trans;
	}

	@Override
	public double quoteBid(double shareNum, double sharePrice) {
		return this.BACKEND.bid(shareNum);
	}

	@Override
	public double quoteAsk(double shareNum, double sharePrice) {
		return this.BACKEND.ask(shareNum);
	}

	@Override
	public SortedMap<Double, Set<Order>> getBuyBook() {
		// Noop
		return null;
	}

	@Override
	public SortedMap<Double, Set<Order>> getSellBook() {
		// Noop
		return null;
	}

	@Override
	public void tick(double time) {
		//Noop
	}

	@Override
	public boolean isShort() {
		return this.SHORT;
	}

}
