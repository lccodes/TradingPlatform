package brown.auctions.bundles;

import brown.auctions.BidBundle;

public class SimpleBidBundle implements BidBundle {
	public final int Bid;
	
	public SimpleBidBundle(int bid) {
		this.Bid = bid;
	}

	@Override
	public int getCost() {
		return this.Bid;
	}
}
