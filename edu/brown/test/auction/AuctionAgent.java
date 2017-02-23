package brown.test.auction;

import brown.agent.Agent;
import brown.auctions.OneSidedWrapper;
import brown.auctions.TwoSidedWrapper;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.Rejection;
import brown.messages.auctions.Bid;
import brown.messages.auctions.TradeRequest;
import brown.messages.markets.MarketUpdate;
import brown.messages.trades.NegotiateRequest;
import brown.setup.Logging;

public class AuctionAgent extends Agent {
	private double myMax;

	public AuctionAgent(String host, int port) throws AgentCreationException {
		super(host, port, new GameSetup());
	}
	
	@Override
	protected void onRegistration(Registration registration) {
	  super.onRegistration(registration);
	  AuctionRegistration auctionRegistration = (AuctionRegistration) registration;
	  this.myMax = auctionRegistration.getValue();
	  Logging.log("[+] max: " + this.myMax);
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
	protected void onTradeRequest(TradeRequest bidRequest) {
		Logging.log("[-] bidRequest for " + bidRequest.AuctionID + " w/ hb " + bidRequest.Current.getCost());
		if (bidRequest.Current.getCost() < this.myMax && bidRequest.Current.getAgent() == null) {
			if (bidRequest.BundleType == BundleType.SimpleOutcry) {
				SimpleBidBundle bundle = new SimpleBidBundle(bidRequest.Current.getCost()+1,
						this.ID,BundleType.SimpleOutcry);
				Bid bid = new Bid(0, bundle, bidRequest.AuctionID, this.ID);
				this.CLIENT.sendTCP(bid);
			} else if (bidRequest.BundleType == BundleType.SimpleSealed){
				SimpleBidBundle bundle = new SimpleBidBundle(this.myMax, this.ID, BundleType.SimpleSealed);
				Bid bid = new Bid(0, bundle, bidRequest.AuctionID, this.ID);
				this.CLIENT.sendTCP(bid);
			}
		}
	}

	@Override
	protected void onNegotiateRequest(NegotiateRequest negotiateRequest) {
		// Noop
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new AuctionAgent("localhost", 2121);
		while(true){}
	}

	@Override
	protected void onSealedBid(OneSidedWrapper market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onOpenOutcry(OneSidedWrapper market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onLMSR(TwoSidedWrapper market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onContinuousDoubleAuction(TwoSidedWrapper market) {
		// TODO Auto-generated method stub
		
	}

}
