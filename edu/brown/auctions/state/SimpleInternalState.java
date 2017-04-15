package brown.auctions.state;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.assets.value.ITradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.interfaces.MarketInternalState;
import brown.messages.auctions.Bid;

public class SimpleInternalState implements MarketInternalState {
	private final Integer ID;
	private final List<Bid> BIDS;
	private final Set<ITradeable> TRADEABLES;
	
	private BidBundle lastAlloc;
	private List<Order> lastPayments;
	private int ticks;
	private BidBundle reserve;
	
	public SimpleInternalState(Integer ID, Set<ITradeable> tradeables) {
		this.BIDS = new LinkedList<Bid>();
		this.lastAlloc = null;
		this.lastPayments = null;
		this.TRADEABLES = tradeables;
		this.ID = ID;
		this.ticks = 0;
		Map<FullType, BidBundle.BidderPrice> reserve = new HashMap<FullType, BidBundle.BidderPrice>();
		for (ITradeable t : this.TRADEABLES) {
			reserve.put(t.getType(), new BidBundle.BidderPrice(null,0));
		}
		this.reserve = new SimpleBidBundle(reserve);
	}
	
	public SimpleInternalState(Integer ID, Set<ITradeable> tradeables, Map<FullType, BidBundle.BidderPrice> reserve) {
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
	public Set<ITradeable> getTradeables() {
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

}
