package brown.securities.mechanisms.cda;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import brown.assets.accounting.Transaction;
import brown.assets.value.SecurityType;
import brown.assets.value.Tradeable;
import brown.auctions.TwoSidedAuction;
import brown.auctions.TwoSidedWrapper;
import brown.auctions.arules.AllocationType;

public class ContinuousDoubleAuction implements TwoSidedAuction {
	private final Integer ID;
	private final Map.Entry<SecurityType, Integer> TYPE;
	private final SortedMap<Double, Set<Transaction>> pendingBuy;
	private final SortedMap<Double, Set<Tradeable>> pendingSell;
	
	/**
	 * For kryonet
	 * DO NOT USE
	 */
	public ContinuousDoubleAuction() {
		this.ID = null;
		this.TYPE = null;
		this.pendingBuy = null;
		this.pendingSell = null;
	}
	
	/**
	 * Constructor
	 * @param ID : auction ID
	 * @param type : SecurityType
	 */
	public ContinuousDoubleAuction(Integer ID, SecurityType type) {
		this.ID = ID;
		this.TYPE = new AbstractMap.SimpleEntry<SecurityType, Integer>(type, null);
		this.pendingBuy = new TreeMap<Double, Set<Transaction>>(Collections.reverseOrder());
		this.pendingSell = new TreeMap<Double, Set<Tradeable>>();
	}
	
	/**
	 * Constructor
	 * @param ID : auction ID
	 * @param type : <SecurityType,Integer>
	 */
	public ContinuousDoubleAuction(Integer ID, Map.Entry<SecurityType, Integer> type) {
		this.ID = ID;
		this.TYPE = type;
		this.pendingBuy = new TreeMap<Double, Set<Transaction>>(Collections.reverseOrder());
		this.pendingSell = new TreeMap<Double, Set<Tradeable>>();
	}

	@Override
	public Integer getID() {
		return this.ID;
	}

	@Override
	public Map.Entry<SecurityType, Integer> getType() {
		return this.TYPE;
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
				Set<Tradeable> toRemove = new HashSet<Tradeable>();
				for (Tradeable opp : opps) {
					if (shareNum <= 0) {
						break;
					}
					double quantity = Math.min(shareNum, opp.getCount());
					completed.add(new Transaction(agentID, opp.getAgentID(), postedSell.getKey()*quantity, quantity, opp));
					shareNum-=quantity;
					if (quantity == opp.getCount()) {
						toRemove.add(opp);
					}
				}
				opps.removeAll(toRemove);
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
	public List<Transaction> ask(Integer agentID, Tradeable opp, double sharePrice) {
		List<Transaction> completed = new LinkedList<Transaction>();
		double shareNum = opp.getCount();
		for (Entry<Double, Set<Transaction>> wanted : this.pendingBuy.entrySet()) {
			if (shareNum <= 0) {
				break;
			}
			
			Set<Transaction> want = wanted.getValue();
			if (wanted.getKey() >= sharePrice && shareNum > 0) {
				Set<Transaction> toRemove = new HashSet<Transaction>();
				for (Transaction buy : want) {
					if (shareNum <= 0) {
						break;
					}
					double quantity = Math.min(opp.getCount(), buy.QUANTITY);
					Tradeable toGive = quantity == opp.getCount() ? opp : opp.split(quantity);
					completed.add(new Transaction(buy.FROM, agentID, sharePrice*quantity, quantity, toGive));
					if (quantity == buy.QUANTITY) {
						toRemove.add(buy);
					} else {
						buy.updateQuantity(buy.QUANTITY - quantity);
					}
					shareNum -= quantity;
				}
				want.removeAll(toRemove);
			} else {
				break;
			}
		}
		
		if (shareNum > 0) {
			Set<Tradeable> wanted = this.pendingSell.get(sharePrice);
			if (wanted == null) {
				wanted = new HashSet<Tradeable>();
			}
			wanted.add(opp);
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
		
		return cost;
	}

	@Override
	public double quoteBid(double shareNum, double sharePrice) {
		double cost = this.cost(shareNum, 0);
		return cost == 0 ? shareNum*sharePrice : cost;
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
}
