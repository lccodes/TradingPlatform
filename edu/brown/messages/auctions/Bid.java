package brown.messages.auctions;

import brown.auctions.BidBundle;
import brown.messages.Message;

public class Bid extends Message {
	public final BidBundle Bundle;
	public final Integer AuctionID;
	public final Integer AgentID;
	
	public Bid() {
		super(null);
		this.AgentID = null;
		this.AuctionID = null;
		this.Bundle = null;
	}

	public Bid(int ID, BidBundle bundle, Integer auctionID, Integer agentID) {
		super(ID);
		this.Bundle = bundle;
		this.AuctionID = auctionID;
		this.AgentID = agentID;
	}
	
	public Bid safeCopy(Integer agentID) {
		return new Bid(this.ID, this.Bundle, this.AuctionID, agentID);
	}
}
