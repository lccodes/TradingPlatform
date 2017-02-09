package brown.securities.mechanisms.cda;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import brown.assets.accounting.Transaction;
import brown.securities.Security;
import brown.securities.SecurityType;
import brown.securities.SecurityWrapper;

public class ContinuousDoubleAuction implements Security {
	private final Integer ID;
	private final SecurityType TYPE;
	private final SortedMap<Double, Transaction> pendingBuy;
	private final SortedMap<Double, Transaction> pendingSell;
	
	public ContinuousDoubleAuction(Integer ID, SecurityType type) {
		this.ID = ID;
		this.TYPE = type;
		this.pendingBuy = new TreeMap<Double, Transaction>(Collections.reverseOrder());
		this.pendingSell = new TreeMap<Double, Transaction>();
	}

	@Override
	public Integer getID() {
		return this.ID;
	}

	@Override
	public SecurityType getType() {
		return this.TYPE;
	}

	@Override
	public List<Transaction> buy(Integer agentID, double shareNum, double sharePrice) {
		List<Transaction> completed = new LinkedList<Transaction>();
		for (double amount : this.pendingSell.keySet()) {
			if (shareNum <= 0) {
				break;
			}
			
			Transaction opp = this.pendingSell.get(amount);
			if (opp.getTransactedPrice() <= sharePrice) {
				shareNum += opp.getCount();
				completed.add(new Transaction(this, opp.getCount(), 
						opp.getAgentID(), opp.getTransactedPrice(), true));
				completed.add(new Transaction(this, Math.abs(opp.getCount()), 
						agentID, opp.getTransactedPrice(), true));
			} else {
				break;
			}
		}
		
		if (shareNum > 0) {
			Transaction pending = new Transaction(this, shareNum, agentID, sharePrice, false);
			this.pendingBuy.put(sharePrice, pending);
			completed.add(pending);
		}
		
		return completed;
	}

	@Override
	public List<Transaction> sell(Integer agentID, double shareNum, double sharePrice) {
		List<Transaction> completed = new LinkedList<Transaction>();
		for (double amount : this.pendingBuy.keySet()) {
			if (shareNum <= 0) {
				break;
			}
			
			Transaction opp = this.pendingBuy.get(amount);
			if (opp.getTransactedPrice() >= sharePrice) {
				shareNum -= opp.getCount();
				completed.add(new Transaction(this, opp.getCount(), 
						opp.getAgentID(), opp.getTransactedPrice(), true));
				completed.add(new Transaction(this, -1 * opp.getCount(), 
						agentID, opp.getTransactedPrice(), true));
			} else {
				break;
			}
		}
		
		if (shareNum > 0) {
			Transaction pending = new Transaction(this, -1 * shareNum, agentID, sharePrice, false);
			this.pendingBuy.put(sharePrice, pending);
			completed.add(pending);
		}
		
		return completed;
	}

	@Override
	public double cost(double newq1, double newq2) {
		double cost = 0;
		for (double amount : this.pendingSell.keySet()) {
			if (newq1 <= 0) {
				break;
			}
			
			cost += this.pendingSell.get(amount).getTransactedPrice() * newq1;
			newq1 += this.pendingSell.get(amount).getCount();
		}
		
		for (double amount : this.pendingBuy.keySet()) {
			if (newq2 <= 0) {
				break;
			}
			
			cost += this.pendingBuy.get(amount).getTransactedPrice() * newq2;
			newq1 -= this.pendingBuy.get(amount).getCount();
		}
		
		return cost == 0 ? Double.MAX_VALUE : cost;
	}

	@Override
	public double bid(double shareNum) {
		return this.cost(shareNum, 0);
	}

	@Override
	public double ask(double shareNum) {
		return this.cost(0, shareNum);
	}

	@Override
	public SecurityWrapper wrap() {
		// TODO Auto-generated method stub
		return null;
	}

}
