package brown.securities.mechanisms.cda;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import brown.agent.Agent;
import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.auctions.twosided.ITwoSidedPriceSetter;
import brown.auctions.twosided.ITwoSidedWrapper;
import brown.messages.markets.MarketOrder;

public class CDAWrapper implements ITwoSidedWrapper, ITwoSidedPriceSetter {
	private final Integer MARKETID;
	private final FullType TYPE;
	private final SortedMap<Double, Double> BUYBOOK;
	private final SortedMap<Double, Double> SELLBOOK;
	
	public CDAWrapper() {
		this.TYPE = null;
		this.MARKETID = null;
		this.BUYBOOK = null;
		this.SELLBOOK = null;
	}
	
	public CDAWrapper(ContinuousDoubleAuction CDA) {
		this.BUYBOOK = new TreeMap<Double, Double>();
		this.SELLBOOK = new TreeMap<Double, Double>();
		
		this.MARKETID = CDA.getID();
		this.TYPE = CDA.getType();
		for (Map.Entry<Double, Set<Order>> entry : CDA.getBuyBook().entrySet()) {
			double count = 0;
			for (Order t : entry.getValue()) {
				count += t.QUANTITY;
			}
			this.BUYBOOK.put(entry.getKey(), count);
		}
		
		for (Map.Entry<Double, Set<Order>> entry : CDA.getSellBook().entrySet()) {
			double count = 0;
			for (Order t : entry.getValue()) {
				count += t.GOOD.getCount();
			}
			this.SELLBOOK.put(entry.getKey(), count);
		}
	}

	@Override
	public Integer getID() {
		return this.MARKETID;
	}

	@Override
	public FullType getType() {
		return this.TYPE;
	}

	@Override
	public void buy(Agent agent, double shareNum, double sharePrice) {
		agent.CLIENT.sendTCP(new MarketOrder(0,this.MARKETID, shareNum, 0, sharePrice));
	}

	@Override
	public void sell(Agent agent, double shareNum, double sharePrice) {
		agent.CLIENT.sendTCP(new MarketOrder(0, this.MARKETID, 0, shareNum, sharePrice));
	}

	@Override
	public double quoteBid(double shareNum, double sharePrice) {
		//TODO: Fix
		return -1;
	}

	@Override
	public double quoteAsk(double shareNum, double sharePrice) {
		//TODO: Fix
		return -1;
	}

	@Override
	public SortedMap<Double, Double> getBuyBook() {
		return this.BUYBOOK;
	}

	@Override
	public SortedMap<Double, Double> getSellBook() {
		return this.SELLBOOK;
	}

	@Override
	public void cancel(Agent agent, boolean buy, double shareNum, double sharePrice) {
		if (buy) {
			this.sell(agent, shareNum, sharePrice);
		} else {
			this.buy(agent, shareNum, sharePrice);
		}
	}

}
