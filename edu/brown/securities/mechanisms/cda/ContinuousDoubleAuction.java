package brown.securities.mechanisms.cda;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import brown.assets.accounting.Account;
import brown.assets.accounting.Precleared;
import brown.assets.accounting.Transaction;
import brown.assets.value.Tradeable;
import brown.assets.value.TradeableManager;
import brown.auctions.TwoSidedAuction;
import brown.auctions.TwoSidedWrapper;
import brown.auctions.arules.AllocationType;
import brown.securities.SecurityType;
import brown.setup.Logging;

public class ContinuousDoubleAuction implements TwoSidedAuction {
	private final Integer ID;
	private final TradeableManager MANAGER;
	private final SortedMap<Double, Set<Transaction>> pendingBuy;
	private final SortedMap<Double, Set<Tradeable>> pendingSell;
	
	/**
	 * For kryonet
	 * DO NOT USE
	 */
	public ContinuousDoubleAuction() {
		this.ID = null;
		this.MANAGER = null;
		this.pendingBuy = null;
		this.pendingSell = null;
	}
	
	/**
	 * Constructor
	 * @param ID : auction ID
	 * @param type : SecurityType
	 */
	public ContinuousDoubleAuction(Integer ID, TradeableManager manager) {
		this.ID = ID;
		this.MANAGER = manager;
		this.pendingBuy = new TreeMap<Double, Set<Transaction>>(Collections.reverseOrder());
		this.pendingSell = new TreeMap<Double, Set<Tradeable>>();
	}

	@Override
	public Integer getID() {
		return this.ID;
	}

	@Override
	public SecurityType getType() {
		return this.MANAGER.getType();
	}

	@Override
	public List<Transaction> bid(Integer agentID, double shareNum, double sharePrice) {
		List<Transaction> completed = new LinkedList<Transaction>();
		for (Map.Entry<Double, Set<Tradeable>> postedSell : this.pendingSell.entrySet()) {
			if (shareNum <= 0) {
				break;
			}
			
			Set<Tradeable> opps = postedSell.getValue();
			if (postedSell.getKey() <= sharePrice && shareNum > 0) {
				for (Tradeable opp : opps) {
					double quantity = Math.min(shareNum, opp.getCount());
					completed.add(new Transaction(agentID, opp.getAgentID(), postedSell.getKey()*quantity, quantity, opp));
					shareNum-=quantity;
				}
			} else {
				break;
			}
		}
		
		if (shareNum > 0) {
			Set<Transaction> wanted = this.pendingBuy.get(sharePrice);
			if (wanted == null) {
				wanted = new HashSet<Transaction>();
			}
			wanted.add(new Transaction(null, agentID, -1, shareNum, null));
			this.pendingBuy.put(sharePrice, wanted);
		}
		
		return completed;
	}

	@Override
	public List<Transaction> ask(Integer agentID, double shareNum, double sharePrice) {
		List<Transaction> completed = new LinkedList<Transaction>();
		for (Entry<Double, Set<Transaction>> wanted : this.pendingBuy.entrySet()) {
			if (shareNum <= 0) {
				break;
			}
			
			Set<Transaction> want = wanted.getValue();
			if (wanted.getKey() >= sharePrice && shareNum > 0) {
				for (Transaction buy : want) {
					double quantity = Math.min(shareNum, buy.QUANTITY);
					completed.add(new Transaction(buy.FROM, agentID, sharePrice*quantity, quantity, null));
					shareNum-=quantity;
				}
			} else {
				break;
			}
		}
		
		if (shareNum > 0) {
			Set<Tradeable> wanted = this.pendingSell.get(sharePrice);
			if (wanted == null) {
				wanted = new HashSet<Tradeable>();
			}
			wanted.add(new Precleared(agentID, shareNum, sharePrice, this.MANAGER.getType()));
			completed.add(new Transaction(null, agentID, sharePrice, shareNum, null));
			this.pendingSell.put(sharePrice, wanted);
		}
		
		return completed;
	}

	/**
	 * TODO: Include sharePrice
	 * @param buyQ
	 * @param sellQ
	 * @return
	 */
	public double cost(double buyQ, double sellQ) {
		double cost = 0;
		for (double price : this.pendingSell.keySet()) {
			if (buyQ <= 0) {
				break;
			}
			
			Set<Tradeable> all = this.pendingSell.get(price);
			for (Tradeable t : all) {
				if (buyQ <= 0) {
					break;
				}
				
				double quantity = Math.min(buyQ, t.getCount());
				cost += quantity*price;
				buyQ -= quantity;
			}
		}
		
		for (double price : this.pendingBuy.keySet()) {
			if (sellQ <= 0) {
				break;
			}
			
			Set<Transaction> all = this.pendingBuy.get(price);
			for (Transaction t : all) {
				if (sellQ <= 0) {
					break;
				}
				
				double quantity = Math.min(buyQ, t.QUANTITY);
				cost += quantity*price;
				sellQ -= quantity;
			}
		}
		
		return cost == 0 ? Double.MAX_VALUE : cost;
	}

	@Override
	public double quoteBid(double shareNum, double sharePrice) {
		return this.cost(shareNum, 0);
	}

	@Override
	public double quoteAsk(double shareNum, double sharePrice) {
		return this.cost(0, shareNum);
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public AllocationType getMechanismType() {
		return AllocationType.ContinuousDoubleAuction;
	}

	@Override
	public SortedMap<Double, Set<Transaction>> getBuyBook() {
		return this.pendingBuy;
	}

	@Override
	public SortedMap<Double, Set<Tradeable>> getSellBook() {
		return this.pendingSell;
	}

	@Override
	public TwoSidedWrapper wrap() {
		return new CDAWrapper(this);
	}

	@Override
	public void tick(double time) {
		// Noop
	}

	@Override
	public Account close(Tradeable t) {
		return this.MANAGER.close(t);
	}

	@Override
	public void fixSellBook(Transaction former, Tradeable toreplace) {
		Set<Tradeable> wanted = this.pendingSell.get(former.COST);
		if (wanted == null) {
			wanted = new HashSet<Tradeable>();
		} else {
			Tradeable getRid = null;
			for (Tradeable t : wanted) {
				if (t.getAgentID().equals(toreplace.getAgentID()) && t.getCount() == toreplace.getCount()) {
					getRid = t;
					break;
				}
			}
			if (getRid != null) {
				wanted.remove(getRid);
			} else {
				Logging.log("[X] missing precleared record " + former.FROM);
			}
		}
		wanted.add(toreplace);
		this.pendingSell.put(former.COST, wanted);
	}

}
