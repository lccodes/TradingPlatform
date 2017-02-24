package brown.securities.mechanisms.cda;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import brown.agent.Agent;
import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.auctions.twosided.TwoSidedPriceSetter;
import brown.auctions.twosided.TwoSidedWrapper;
import brown.messages.markets.PurchaseRequest;

public class CDAWrapper implements TwoSidedWrapper, TwoSidedPriceSetter {
	private final ContinuousDoubleAuction CDA;
	private final SortedMap<Double, Double> BUYBOOK;
	private final SortedMap<Double, Double> SELLBOOK;
	
	public CDAWrapper() {
		this.CDA = null;
		this.BUYBOOK = null;
		this.SELLBOOK = null;
	}
	
	public CDAWrapper(ContinuousDoubleAuction CDA) {
		this.CDA = CDA;
		this.BUYBOOK = new TreeMap<Double, Double>();
		this.SELLBOOK = new TreeMap<Double, Double>();
		for (Map.Entry<Double, Set<Order>> entry : this.CDA.getBuyBook().entrySet()) {
			double count = 0;
			for (Order t : entry.getValue()) {
				count += t.QUANTITY;
			}
			this.BUYBOOK.put(entry.getKey(), count);
		}
		
		for (Map.Entry<Double, Set<Order>> entry : this.CDA.getSellBook().entrySet()) {
			double count = 0;
			for (Order t : entry.getValue()) {
				count += t.GOOD.getCount();
			}
			this.SELLBOOK.put(entry.getKey(), count);
		}
	}

	@Override
	public Integer getID() {
		return CDA.getID();
	}

	@Override
	public FullType getType() {
		return CDA.getType();
	}

	@Override
	public void buy(Agent agent, double shareNum, double sharePrice) {
		agent.CLIENT.sendTCP(new PurchaseRequest(0,this.CDA, shareNum, sharePrice));
	}

	@Override
	public void sell(Agent agent, double shareNum, double sharePrice) {
		agent.CLIENT.sendTCP(new PurchaseRequest(this.CDA, shareNum, sharePrice, 0));
	}

	@Override
	public double quoteBid(double shareNum, double sharePrice) {
		return this.CDA.quoteBid(shareNum, sharePrice);
	}

	@Override
	public double quoteAsk(double shareNum, double sharePrice) {
		return this.CDA.quoteAsk(shareNum, sharePrice);
	}

	@Override
	public SortedMap<Double, Double> getBuyBook() {
		return this.BUYBOOK;
	}

	@Override
	public SortedMap<Double, Double> getSellBook() {
		return this.SELLBOOK;
	}

}
