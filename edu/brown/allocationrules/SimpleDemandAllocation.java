package brown.allocationrules;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import brown.assets.value.FullType;
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

public class SimpleDemandAllocation implements AllocationRule {
	private Map<FullType, MarketState> lastDemand;

	public SimpleDemandAllocation() {
		this.lastDemand = new HashMap<FullType, MarketState>();
	}

	@Override
	public BidBundle getAllocation(MarketInternalState state) {
		//System.out.println("WHUT " + state.getBids());
		Map<FullType, MarketState> highest = new HashMap<FullType, MarketState>();
		for (Tradeable trade : state.getTradeables()) {
			MarketState lastHigh = this.lastDemand.getOrDefault(
					trade.getType(), new MarketState(null, 0));
			boolean updated = false;
			for (Bid bid : state.getBids()) {
				if (updated) {
					break;
				}

				if (bid.Bundle.getType().equals(BundleType.Simple)) {
					SimpleBidBundle bundle = (SimpleBidBundle) bid.Bundle;
					if (bundle.isDemanded(trade.getType())
							&& !bid.AgentID.equals(lastHigh.AGENTID)) {
						updated = true;
						highest.put(trade.getType(), new MarketState(
								bid.AgentID, lastHigh.PRICE));
					}
				} else {
					Logging.log("[X] Incorrect bundle type by " + bid.AgentID
							+ " in auction " + bid.AuctionID);
				}
			}
		}

		state.clearBids();
		for (Entry<FullType, MarketState> entry : highest.entrySet()) {
			this.lastDemand.put(
					entry.getKey(),
					new MarketState(entry.getValue().AGENTID, entry
							.getValue().PRICE + state.getIncrement()));
		}
		for (Tradeable t : state.getTradeables()) {
			if (!highest.containsKey(t.getType())) {
				highest.put(t.getType(), this.lastDemand.getOrDefault(
						t.getType(), new MarketState(null, 0)));
			}
		}
		//System.out.println(this.lastDemand);
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
