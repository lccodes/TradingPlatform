package brown.marketinternalstates;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.bundles.BidBundle;
import brown.bundles.MarketState;
import brown.bundles.SimpleBidBundle;
import brown.messages.auctions.Bid;
import brown.tradeables.Tradeable;

public class SimpleInternalState implements MarketInternalState {
	private final double INCREMENT = 20.0;
	private final Integer ID;
	private final List<Bid> BIDS;
	private final Set<Tradeable> TRADEABLES;
	
	private BidBundle lastAlloc;
	private List<Order> lastPayments;
	private int ticks;
	private BidBundle reserve;
	private boolean maximizing;
	
	public SimpleInternalState(Integer ID, Set<Tradeable> tradeables) {
		this.BIDS = new LinkedList<Bid>();
		this.lastAlloc = null;
		this.lastPayments = null;
		this.TRADEABLES = tradeables;
		this.ID = ID;
		this.ticks = 0;
		Map<FullType, MarketState> reserve = new HashMap<FullType, MarketState>();
		for (Tradeable t : this.TRADEABLES) {
			reserve.put(t.getType(), new MarketState(null,0));
		}
		this.reserve = new SimpleBidBundle(reserve);
		this.maximizing = false;
	}
	
	public SimpleInternalState(Integer ID, Set<Tradeable> tradeables, Map<FullType, MarketState> reserve) {
		this.BIDS = new LinkedList<Bid>();
		this.lastAlloc = null;
		this.lastPayments = null;
		this.TRADEABLES = tradeables;
		this.ID = ID;
		this.ticks = 0;
		this.reserve = new SimpleBidBundle(reserve);
	}

	@Override
	public void addBid(Bid bid) {
		this.ticks = 0;
		this.BIDS.add(bid);
	}

	@Override
	public void setAllocation(BidBundle allocation) {
		this.lastAlloc = allocation;
	}

	@Override
	public List<Bid> getBids() {
		return this.BIDS;
	}

	@Override
	public Set<Tradeable> getTradeables() {
		return this.TRADEABLES;
	}

	@Override
	public BidBundle getAllocation() {
		return this.lastAlloc;
	}

	@Override
	public Integer getID() {
		return this.ID;
	}

	@Override
	public void setPayments(List<Order> payments) {
		this.lastPayments = payments;
	}

	@Override
	public List<Order> getPayments() {
		return this.lastPayments;
	}

	@Override
	public void tick(long time) {
		this.ticks++;		
	}

	@Override
	public int getTicks() {
		return this.ticks;
	}

	@Override
	public void setReserve(BidBundle o) {
		this.reserve = o;
	}

	@Override
	public BidBundle getReserve() {
		return this.reserve;
	}

	@Override
	public void clearBids() {
		this.BIDS.clear();
	}

	@Override
	public double getIncrement() {
		return this.INCREMENT;
	}

	@Override
	public void setMaximizingRevenue(boolean b) {
		this.maximizing = b;
	}

	@Override
	public boolean isMaximizingRevenue() {
		return this.maximizing;
	}

	@Override
	public int getEligibility() {
		int elig = 0;
		if (this.reserve == null) {
			return 0;
		}
		SimpleBidBundle bundle = (SimpleBidBundle) this.reserve;
		for (FullType type : bundle.getDemandSet()) {
			MarketState state = bundle.getBid(type);
			if (state != null && state.AGENTID != null) {
				elig+=1;
			}
		}
		return elig;
	}
}
