package brown.rules.allocationrules;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import brown.assets.value.FullType;
import brown.assets.value.TradeableType;
import brown.auctions.arules.MechanismType;
import brown.bundles.BidBundle;
import brown.bundles.BundleType;
import brown.bundles.MarketState;
import brown.bundles.SimpleBidBundle;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.setup.Logging;
import brown.tradeables.Tradeable;


public class SimpleHighestBidderAllocation implements AllocationRule {
  //this needs to not be null
	private final Map<FullType, MarketState> RESERVE;
	public SimpleHighestBidderAllocation(Map<FullType, MarketState> reserve) {
		this.RESERVE = reserve;
	}
	public SimpleHighestBidderAllocation() {
		this.RESERVE = new HashMap<FullType, MarketState>();
	}
	
	@Override 
	public BidBundle getAllocation(MarketInternalState state) {
		System.out.println("BIDS? " + state.getBids());
		Map<FullType, MarketState> highest = new HashMap<FullType, MarketState>();
		MarketState def = new MarketState(null,0);
		for (Tradeable trade : state.getTradeables()) {
			MarketState maxBidder = this.RESERVE.getOrDefault(trade.getType(), def);
			//where are these bids coming from? 
			for (Bid bid : state.getBids()) {
				if (bid.Bundle.getType().equals(BundleType.Simple)) {
					SimpleBidBundle bundle = (SimpleBidBundle) bid.Bundle;
					//the problem is THIS is null below
					//so the bundle's bid has no type attached to it. 
					MarketState bp = bundle.getBid(trade.getType());
					//System.out.println(trade.getType());
					if (bp != null && bp.PRICE > maxBidder.PRICE) {
						maxBidder = new MarketState(bid.AgentID, bp.PRICE);
					}
				} else {
					Logging.log("[X] Incorrect bundle type by " + bid.AgentID + " in auction " + bid.AuctionID);
				}
			}
			highest.put(trade.getType(), maxBidder);
		}
		return new SimpleBidBundle(highest);
	}
	@Override
	public void tick(long time) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Map<Integer, Set<Tradeable>> getAllocations(Set<Bid> bids,
			Set<Tradeable> items) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public BidRequest getBidRequest(Set<Bid> bids, Integer iD) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isPrivate() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isOver() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public BundleType getBundleType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Set<Bid> withReserve(Set<Bid> bids) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isValid(Bid bid, Set<Bid> bids) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public MechanismType getAllocationType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public GameReport getReport() {
		// TODO Auto-generated method stub
		return null;
	}

}
