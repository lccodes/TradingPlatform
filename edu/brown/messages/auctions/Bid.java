package brown.messages.auctions;

import java.util.Comparator;

import brown.auctions.BidBundle;
import brown.messages.Message;

public class Bid extends Message {
	public final BidBundle Bundle;
	public final Integer AuctionID;
	public final Integer AgentID;
	
	/**
	 * Empty constructor for kryo
	 * DO NOT USE
	 */
	public Bid() {
		super(null);
		this.AgentID = null;
		this.AuctionID = null;
		this.Bundle = null;
	}

	/**
	 * Bid for when an agent wants to bid in an auction
	 * @param ID : bid ID
	 * @param bundle : bid bundle; varies by what the auction wants
	 * @param auctionID : auction's ID
	 * @param agentID : agent's ID; verified by server
	 */
	public Bid(int ID, BidBundle bundle, Integer auctionID, Integer agentID) {
		super(ID);
		this.Bundle = bundle;
		this.AuctionID = auctionID;
		this.AgentID = agentID;
	}
	
	/**
	 * Safe copy for server during ID verification
	 * @param agentID : truthful ID
	 * @return Bid
	 */
	public Bid safeCopy(Integer agentID) {
		return new Bid(this.ID, this.Bundle, this.AuctionID, agentID);
	}
	
	public static class BidComparator implements Comparator<Bid> {
		private final boolean ASC;
		
		public BidComparator(boolean ascending) {
			this.ASC = ascending;
		}

		@Override
		public int compare(Bid o1, Bid o2) {
			if (this.ASC) {
				return new Double(o1.Bundle.getCost()).compareTo(new Double(o2.Bundle.getCost()));
			} else {
				return new Double(o2.Bundle.getCost()).compareTo(new Double(o1.Bundle.getCost()));
			}
		}
		
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Bid)) {
			return false;
		}
		
		Bid o = (Bid) other;
		return this.ID == o.ID;
	}
}
