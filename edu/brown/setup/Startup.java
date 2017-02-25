package brown.setup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import brown.agent.Agent;
import brown.assets.accounting.Account;
import brown.assets.accounting.Order;
import brown.assets.value.AgentSecurity;
import brown.assets.value.FullType;
import brown.assets.value.Security;
import brown.assets.value.SecurityType;
import brown.assets.value.Tradeable;
import brown.auctions.arules.MechanismType;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.auctions.crules.ClosestMatchClearing;
import brown.auctions.crules.LMSRNoClearing;
import brown.auctions.crules.LMSRYesClearing;
import brown.auctions.crules.LowestPriceClearing;
import brown.auctions.onesided.OneSidedWrapper;
import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.auctions.prules.PaymentType;
import brown.auctions.rules.ClearingRule;
import brown.auctions.twosided.TwoSidedAuction;
import brown.auctions.twosided.TwoSidedWrapper;
import brown.messages.BankUpdate;
import brown.messages.Message;
import brown.messages.Registration;
import brown.messages.Rejection;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidReqeust;
import brown.messages.markets.MarketOrder;
import brown.messages.markets.TradeRequest;
import brown.messages.trades.NegotiateRequest;
import brown.messages.trades.Trade;
import brown.securities.mechanisms.cda.CDAWrapper;
import brown.securities.mechanisms.cda.ContinuousDoubleAuction;
import brown.securities.mechanisms.lmsr.LMSR;
import brown.securities.mechanisms.lmsr.LMSRBackend;
import brown.securities.mechanisms.lmsr.LMSRWrapper;

import com.esotericsoftware.kryo.Kryo;

public final class Startup {
	
	//TODO: Consider reflection for dynamic loading
	public static boolean start(Kryo kryo) {
		kryo.register(java.util.LinkedList.class);
		kryo.register(ArrayList.class);
		kryo.register(Set.class);
		kryo.register(TreeSet.class);
		kryo.register(HashSet.class);
		kryo.register(TreeMap.class);
		kryo.register(java.util.Collections.reverseOrder().getClass());
		
		kryo.register(Agent.class);
		kryo.register(Message.class);
		kryo.register(BankUpdate.class);
		kryo.register(Bid.class);
		kryo.register(BidReqeust.class);
		kryo.register(Registration.class);
		kryo.register(Trade.class);
		kryo.register(NegotiateRequest.class);
		kryo.register(Account.class);
		kryo.register(Tradeable.class);
		kryo.register(TradeRequest.class);
		kryo.register(Rejection.class);
		kryo.register(LMSRBackend.class);
		kryo.register(Timestamp.class);
		kryo.register(Date.class);
		kryo.register(BidBundle.class);
		kryo.register(SimpleBidBundle.class);
		kryo.register(BundleType.class);
		kryo.register(MarketOrder.class);
		kryo.register(TwoSidedAuction.class);
		kryo.register(TwoSidedWrapper.class);
		kryo.register(MechanismType.class);
		kryo.register(PaymentType.class);
		kryo.register(LMSRWrapper.class);
		kryo.register(CDAWrapper.class);
		kryo.register(LMSR.class);
		kryo.register(ClearingRule.class);
		kryo.register(LMSRYesClearing.class);
		kryo.register(LMSRNoClearing.class);
		kryo.register(SecurityType.class);
		kryo.register(FullType.class);
		kryo.register(Security.class);
		kryo.register(AgentSecurity.class);
		kryo.register(ContinuousDoubleAuction.class);
		kryo.register(ClosestMatchClearing.class);
		kryo.register(LowestPriceClearing.class);
		kryo.register(Order.class);
		kryo.register(OneSidedWrapper.class);
		kryo.register(SimpleOneSidedWrapper.class);
		
		return true;
	}

}
