package brown.auctions.crules;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

import brown.assets.accounting.Order;
import brown.assets.value.ITradeable;
import brown.auctions.rules.ClearingRule;

public class LowestPriceClearing implements ClearingRule {
	private final SortedMap<Double, Set<Order>> buyOrderBook;
	private final SortedMap<Double, Set<Order>> sellOrderBook;
	
	private final boolean SHORT;
	private final Function<Double, ITradeable> SHORTER;
	
	public LowestPriceClearing() {
		this.buyOrderBook = new TreeMap<Double, Set<Order>>(Collections.reverseOrder());
		this.sellOrderBook = new TreeMap<Double, Set<Order>>();
		this.SHORT = false;
		this.SHORTER = null;
	}
	
	public LowestPriceClearing(Function<Double, ITradeable> shorter) {
		this.buyOrderBook = new TreeMap<Double, Set<Order>>(Collections.reverseOrder());
		this.sellOrderBook = new TreeMap<Double, Set<Order>>();
		this.SHORT = true;
		this.SHORTER = shorter;
	}

	@Override
	public List<Order> buy(Integer agentID, double shareNum, double sharePrice) {
		List<Order> completed = new LinkedList<Order>();
		for (Map.Entry<Double, Set<Order>> postedSell : this.sellOrderBook.entrySet()) {
			if (shareNum <= 0) {
				break;
			}
			
			Set<Order> opps = postedSell.getValue();
			if (postedSell.getKey() <= sharePrice && shareNum > 0) {
				Set<Order> toRemove = new HashSet<Order>();
				for (Order opp : opps) {
					if (shareNum <= 0) {
						break;
					} else if (!opp.GOOD.getAgentID().equals(opp.FROM)) {
						toRemove.add(opp);
						continue;
					}
					
					double quantity = Math.min(shareNum, opp.GOOD.getCount());
					completed.add(new Order(agentID, opp.FROM, postedSell.getKey()*quantity, quantity, opp.GOOD));
					shareNum-=quantity;
					if (quantity == opp.GOOD.getCount()) {
						toRemove.add(opp);
					}
				}
				opps.removeAll(toRemove);
			} else {
				break;
			}
		}
		
		if (shareNum > 0) {
			Set<Order> wanted = this.buyOrderBook.get(sharePrice);
			if (wanted == null) {
				wanted = new HashSet<Order>();
			}
			wanted.add(new Order(null, agentID, -1, shareNum, null));
			this.buyOrderBook.put(sharePrice, wanted);
		}
		
		return completed;
	}

	@Override
	public List<Order> sell(Integer agentID, ITradeable opp, double sharePrice) {
		List<Order> completed = new LinkedList<Order>();
		double shareNum = opp.getCount();
		if (this.SHORT && opp.getAgentID() == null) {
			opp = this.SHORTER.apply(opp.getCount());
		} else if (opp.getAgentID() == null) {
			return completed;
		}
		
		for (Entry<Double, Set<Order>> wanted : this.buyOrderBook.entrySet()) {
			if (shareNum <= 0) {
				break;
			}
			
			Set<Order> want = wanted.getValue();
			if (wanted.getKey() >= sharePrice && shareNum > 0) {
				Set<Order> toRemove = new HashSet<Order>();
				for (Order buy : want) {
					if (shareNum <= 0) {
						break;
					}
					
					double quantity = Math.min(opp.getCount(), buy.QUANTITY);
					ITradeable toGive = quantity == opp.getCount() ? opp : opp.split(quantity);
					completed.add(new Order(buy.FROM, agentID, sharePrice*quantity, quantity, toGive));
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
			Set<Order> wanted = this.sellOrderBook.get(sharePrice);
			if (wanted == null) {
				wanted = new HashSet<Order>();
			}
			wanted.add(new Order(null, opp.getAgentID(), sharePrice*shareNum, opp.getCount(), opp));
			this.sellOrderBook.put(sharePrice, wanted);
		}
		
		return completed;
	}

	@Override
	public double quoteBid(double shareNum, double sharePrice) {
		double cost = 0;
		double buyQ = shareNum;
		for (double price : this.sellOrderBook.keySet()) {
			if (buyQ <= 0) {
				break;
			}
			
			Set<Order> all = this.sellOrderBook.get(price);
			for (Order t : all) {
				if (buyQ <= 0) {
					break;
				}
				
				double quantity = Math.min(buyQ, t.GOOD.getCount());
				cost += quantity*price;
				buyQ -= quantity;
			}
		}
		
		return buyQ > 0 ? cost + buyQ*sharePrice : cost; 
	}

	@Override
	public double quoteAsk(double shareNum, double sharePrice) {
		double sellQ = shareNum;
		double cost = 0;
		
		for (double price : this.buyOrderBook.keySet()) {
			if (sellQ <= 0) {
				break;
			}
			
			Set<Order> all = this.buyOrderBook.get(price);
			for (Order t : all) {
				if (sellQ <= 0) {
					break;
				}
				
				double quantity = Math.min(sellQ, t.QUANTITY);
				cost += quantity*price;
				sellQ -= quantity;
			}
		}
		
		return sellQ > 0 ? cost + sellQ*sharePrice : cost;
	}

	@Override
	public SortedMap<Double, Set<Order>> getBuyBook() {
		return this.getBuyBook();
	}

	@Override
	public SortedMap<Double, Set<Order>> getSellBook() {
		return this.getSellBook();
	}

	@Override
	public void tick(double time) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isShort() {
		return this.SHORT;
	}

}
