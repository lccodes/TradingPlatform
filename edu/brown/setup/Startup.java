package brown.setup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import brown.agent.Agent;
import brown.assets.accounting.Account;
import brown.assets.accounting.TransactionOld;
import brown.assets.value.AgentSecurity;
import brown.assets.value.FullType;
import brown.assets.value.Security;
import brown.assets.value.SecurityType;
import brown.assets.value.Tradeable;
import brown.auctions.TwoSidedAuction;
import brown.auctions.TwoSidedWrapper;
import brown.auctions.arules.AllocationType;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.prules.PaymentType;
import brown.messages.BankUpdate;
import brown.messages.Message;
import brown.messages.Registration;
import brown.messages.Rejection;
import brown.messages.auctions.Bid;
import brown.messages.auctions.TradeRequest;
import brown.messages.markets.LimitOrder;
import brown.messages.markets.MarketUpdate;
import brown.messages.markets.PurchaseRequest;
import brown.messages.trades.NegotiateRequest;
import brown.messages.trades.Trade;
import brown.securities.SecurityOld;
import brown.securities.SecurityWrapper;
import brown.securities.mechanisms.cda.CDAWrapper;
import brown.securities.mechanisms.lmsr.LMSRBackend;
import brown.securities.mechanisms.lmsr.LMSRNo;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
import brown.securities.mechanisms.lmsr.LMSRYes;
import brown.securities.prediction.PredictionMarket;
import brown.securities.prediction.structures.PMNo;
import brown.securities.prediction.structures.PMYes;

import com.esotericsoftware.kryo.Kryo;

public final class Startup {
	
	//TODO: Consider reflection for dynamic loading
	public static boolean start(Kryo kryo) {
		kryo.register(java.util.LinkedList.class);
		kryo.register(ArrayList.class);
		kryo.register(Set.class);
		kryo.register(TreeSet.class);
		kryo.register(HashSet.class);
		
		kryo.register(Agent.class);
		kryo.register(Message.class);
		kryo.register(BankUpdate.class);
		kryo.register(Bid.class);
		kryo.register(TradeRequest.class);
		kryo.register(PurchaseRequest.class);
		kryo.register(Registration.class);
		kryo.register(Trade.class);
		kryo.register(NegotiateRequest.class);
		kryo.register(SecurityOld.class);
		kryo.register(PredictionMarket.class);
		kryo.register(Account.class);
		kryo.register(Tradeable.class);
		kryo.register(MarketUpdate.class);
		kryo.register(Rejection.class);
		kryo.register(SecurityWrapper.class);
		kryo.register(PMYes.class);
		kryo.register(TransactionOld.class);
		kryo.register(LMSRBackend.class);
		kryo.register(PMNo.class);
		kryo.register(Timestamp.class);
		kryo.register(Date.class);
		kryo.register(BidBundle.class);
		kryo.register(SimpleBidBundle.class);
		kryo.register(BundleType.class);
		kryo.register(LimitOrder.class);
		kryo.register(TwoSidedAuction.class);
		kryo.register(TwoSidedWrapper.class);
		kryo.register(AllocationType.class);
		kryo.register(PaymentType.class);
		kryo.register(LMSRWrapper.class);
		kryo.register(CDAWrapper.class);
		kryo.register(LMSRYes.class);
		kryo.register(LMSRNo.class);
		kryo.register(SecurityType.class);
		kryo.register(FullType.class);
		kryo.register(Security.class);
		kryo.register(AgentSecurity.class);
		
		return true;
	}

}
