package brown.auctions.crules;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Account;
import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.assets.value.Tradeable;
import brown.assets.value.TradeableType;
import brown.auctions.rules.ClearingRule;
import brown.securities.mechanisms.lmsr.LMSRBackend;

public class LMSRNoClearing implements ClearingRule {
	private final LMSRBackend BACKEND;
	private final FullType TYPE;
	private final boolean SHORT;

	public LMSRNoClearing() {
		this.BACKEND = null;
		this.TYPE = null;
		this.SHORT = false;
	}

	public LMSRNoClearing(LMSRBackend backend, boolean shortSelling) {
		this.BACKEND = backend;
		this.TYPE = new FullType(TradeableType.PredictionNo, backend.getId());
		this.SHORT = shortSelling;
	}

	@Override
	public List<Order> buy(Integer agentID, double shareNum, double sharePrice) {
		double cost = this.BACKEND.ask(shareNum);
		this.BACKEND.no(null, shareNum);
		List<Order> trans = new LinkedList<Order>();
		Tradeable newSec = new Tradeable(this.TYPE,  shareNum, agentID, state -> {
			List<Account> list = new LinkedList<Account>();
			if (state.STATE.getState() == 0) {
				list.add(new Account(null).add(state.QUANTITY));
			}
			return list;
		});
		trans.add(new Order(agentID, null, cost, shareNum, newSec));
		return trans;
	}

	@Override
	public List<Order> sell(Integer agentID, Tradeable opp, double sharePrice) {
		List<Order> trans = new LinkedList<Order>();
		if (this.SHORT && opp.getAgentID() == null) {
			Tradeable newSec = new Tradeable(this.TYPE, opp.getCount(), agentID, state -> {
				List<Account> list = new LinkedList<Account>();
				if (state.STATE.getState() == 0) {
					list.add(new Account(null).add(state.QUANTITY));
				}
				return list;
			});
			opp = newSec;
		} else if (opp.getAgentID() == null) {
			return trans;
		}
		double cost = this.BACKEND.ask(-1 * opp.getCount());
		this.BACKEND.no(null, -1 * opp.getCount());
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
		// Noop
	}

	@Override
	public boolean isShort() {
		return this.SHORT;
	}

	@Override
	public void cancel(Integer agentID, boolean buy, double shareNum, double sharePrice) {
		// Noop
	}

	@Override
	public double price() {
		return this.BACKEND.price(false);
	}

}
