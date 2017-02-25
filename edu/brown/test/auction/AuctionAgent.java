package brown.test.auction;

import brown.agent.Agent;
import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.Rejection;
import brown.messages.auctions.BidReqeust;
import brown.messages.markets.TradeRequest;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.cda.CDAWrapper;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
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
	protected void onMarketUpdate(TradeRequest marketUpdate) {
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
	protected void onTradeRequest(BidReqeust bidRequest) {
		Logging.log("[-] bidRequest for " + bidRequest.AuctionID + " w/ hb " + bidRequest.Current.getCost());
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
	protected void onLMSR(LMSRWrapper market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onContinuousDoubleAuction(CDAWrapper market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onSimpleSealed(SimpleOneSidedWrapper market) {
		Logging.log("[-] bidRequest for " + market.getAuctionID() + " w/ hb " + market.getQuote().getCost());
		market.bid(this, this.myMax);
	}

	@Override
	protected void onSimpleOpenOutcry(SimpleOneSidedWrapper market) {
		Logging.log("[-] bidRequest for " + market.getAuctionID() + " w/ hb " + market.getQuote().getCost());
		if (market.getQuote().getAgent() == null && market.getQuote().getCost() < this.myMax) {
			market.bid(this, market.getQuote().getCost()+1);
		}
	}

}
