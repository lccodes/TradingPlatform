package brown.test.auction;

import java.util.HashMap;
import java.util.Map;

import brown.agent.Agent;
import brown.assets.value.FullType;
import brown.auctions.wrappers.SimpleWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.Registration;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
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
		if (message.REJECTED) {
			Logging.log("[-] rejected? " + message.failedBR);
		}
	}

	@Override
	public void onMarketUpdate(GameReport marketUpdate) {
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
	public void onTradeRequest(BidRequest bidRequest) {
		Logging.log("[-] bidRequest for " + bidRequest.AuctionID + " w/ hb " + bidRequest.Current.getCost());
	}

	@Override
	public void onNegotiateRequest(NegotiateRequest negotiateRequest) {
		// Noop
	}

	@Override
	public void onLMSR(LMSRWrapper market) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContinuousDoubleAuction(CDAWrapper market) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new AuctionAgent("localhost", 2121);
		while(true){}
	}

	@Override
	public void onSimpleSealed(SimpleWrapper market) {
		for (FullType type : market.getTradeables()) {
			Logging.log("[-] bidRequest for " + market.getAuctionID() + " w/ hb " + market.getMarketState(type).PRICE);
			Map<FullType, Double> bids = new HashMap<FullType, Double>();
			bids.put(type, this.myMax);
			market.bid(this, bids);
		}
	}

	@Override
	public void onSimpleOpenOutcry(SimpleWrapper market) {
		for (FullType type : market.getTradeables()) {
			Logging.log("[-] bidRequest for " + market.getAuctionID() + " w/ hb " + market.getMarketState(type).PRICE);
			if (market.getMarketState(type).AGENTID == null && market.getMarketState(type).PRICE < this.myMax) {
				Map<FullType, Double> bids = new HashMap<FullType, Double>();
				bids.put(type, market.getMarketState(type).PRICE+1);
				market.bid(this, bids);
			}
		}
	}

}
