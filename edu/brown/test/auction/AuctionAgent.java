package brown.test.auction;

import brown.agent.Agent;
import brown.auctions.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.Rejection;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.MarketUpdate;
import brown.messages.trades.TradeRequest;
import brown.setup.Logging;

public class AuctionAgent extends Agent {
	private final double myMax;

	public AuctionAgent(String host, int port) throws AgentCreationException {
		super(host, port);
		this.CLIENT.getKryo().register(TheGood.class);
		this.myMax = Math.random() * 100;
		Logging.log("[+] max: " + myMax);
	}

	@Override
	protected void onRejection(Rejection message) {
		Logging.log("[x] rejected: " + message.failedBR);
	}

	@Override
	protected void onMarketUpdate(MarketUpdate marketUpdate) {
		//Noop
	}

	@Override
	protected void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("[-] bank " + bankUpdate.newAccount.monies);
		if (bankUpdate.newAccount.goods.size() > 0) {
			Logging.log("[+] victory!");
		}
	}

	@Override
	protected void onBidRequest(BidRequest bidRequest) {
		Logging.log("[-] bidRequest for " + bidRequest.AuctionID + " w/ hb " + bidRequest.HighBidderID);
		if (bidRequest.CurrentPrice < this.myMax && !bidRequest.HighBidder
				&& bidRequest.BundleType == BundleType.Simple) {
			SimpleBidBundle bundle = new SimpleBidBundle(bidRequest.CurrentPrice+1, this.ID);
			Bid bid = new Bid(0, bundle, bidRequest.AuctionID, this.ID);
			this.CLIENT.sendTCP(bid);
		}
	}

	@Override
	protected void onTradeRequest(TradeRequest tradeRequest) {
		// Noop
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new AuctionAgent("localhost", 9898);
		while(true){}
	}

}
