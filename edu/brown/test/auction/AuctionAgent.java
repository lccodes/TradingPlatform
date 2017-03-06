package brown.test.auction;

import brown.agent.Agent;
import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.auctions.BidReqeust;
import brown.messages.markets.MarketUpdate;
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
	public void onRegistration(Registration registration) {
	  super.onRegistration(registration);
	  AuctionRegistration auctionRegistration = (AuctionRegistration) registration;
	  this.myMax = auctionRegistration.getValue();
	  Logging.log("[+] max: " + this.myMax);
	}

	@Override
	public void onAck(Ack message) {
		Logging.log("[x] rejected: " + message.failedBR);
	}

	@Override
	public void onMarketUpdate(MarketUpdate marketUpdate) {
		//Noop
	}

	@Override
	public void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("[-] bank " + bankUpdate.newAccount.monies);
		if (bankUpdate.newAccount.tradeables.size() > 0) {
			Logging.log("[+] victory!");
		}
	}

	@Override
	public void onTradeRequest(BidReqeust bidRequest) {
		Logging.log("[-] bidRequest for " + bidRequest.AuctionID + " w/ hb " + bidRequest.Current.getCost());
	}

	@Override
	public void onNegotiateRequest(NegotiateRequest negotiateRequest) {
		// Noop
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new AuctionAgent("localhost", 2121);
		while(true){}
	}

	@Override
	public void onLMSR(LMSRWrapper market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContinuousDoubleAuction(CDAWrapper market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSimpleSealed(SimpleOneSidedWrapper market) {
		Logging.log("[-] bidRequest for " + market.getAuctionID() + " w/ hb " + market.getQuote().getCost());
		market.bid(this, this.myMax);
	}

	@Override
	public void onSimpleOpenOutcry(SimpleOneSidedWrapper market) {
		Logging.log("[-] bidRequest for " + market.getAuctionID() + " w/ hb " + market.getQuote().getCost());
		if (market.getQuote().getAgent() == null && market.getQuote().getCost() < this.myMax) {
			market.bid(this, market.getQuote().getCost()+1);
		}
	}

}
