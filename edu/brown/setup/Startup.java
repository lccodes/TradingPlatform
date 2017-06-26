package brown.setup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import brown.agent.Agent;
import brown.allocationrules.OpenOutcryRule;
import brown.allocationrules.SealedBidRule;
import brown.assets.accounting.Account;
import brown.assets.accounting.Ledger;
import brown.assets.accounting.Order;
import brown.assets.accounting.Transaction;
import brown.assets.value.FullType;
import brown.assets.value.TradeableType;
import brown.auctions.arules.MechanismType;
import brown.bundles.BidBundle;
import brown.bundles.BundleType;
import brown.bundles.MarketState;
import brown.bundles.SimpleBidBundle;
import brown.markets.CDAServer;
import brown.markets.ContinuousDoubleAuction;
import brown.markets.ITwoSidedAuction;
import brown.markets.LMSR;
import brown.markets.LMSRBackend;
import brown.markets.LMSRServer;
import brown.markets.LiquiditySensitive;
import brown.markets.LukeMM;
import brown.markets.SimpleAuction;
import brown.markets.TwoSidedAuction;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.Message;
import brown.messages.Registration;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.GameReport;
import brown.messages.markets.MarketOrder;
import brown.messages.markets.TradeRequest;
import brown.messages.trades.NegotiateRequest;
import brown.messages.trades.Trade;
import brown.paymentrules.PaymentType;
import brown.paymentrules.library.FirstPriceRule;
import brown.paymentrules.library.SecondPriceRule;
import brown.rules.clearingrules.ClearingRule;
import brown.rules.clearingrules.ClosestMatchClearing;
import brown.rules.clearingrules.LMSRNoClearing;
import brown.rules.clearingrules.LMSRYesClearing;
import brown.rules.clearingrules.LowestPriceClearing;
import brown.tradeables.Tradeable;

import com.esotericsoftware.kryo.Kryo;

public final class Startup {
	
	//TODO: Consider reflection for dynamic loading
	public static boolean start(Kryo kryo) {
		kryo.register(IllegalArgumentException.class);
		kryo.register(java.util.LinkedList.class);
		kryo.register(ArrayList.class);
		kryo.register(Set.class);
		kryo.register(TreeSet.class);
		kryo.register(HashSet.class);
		kryo.register(TreeMap.class);
		kryo.register(java.util.Collections.reverseOrder().getClass());
		kryo.register(int[].class);
		
		kryo.register(Agent.class);
		kryo.register(Message.class);
		kryo.register(BankUpdate.class);
		kryo.register(GameReport.class);
		kryo.register(Bid.class);
		kryo.register(BidRequest.class);
		kryo.register(Transaction.class);
		kryo.register(Registration.class);
		kryo.register(Trade.class);
		kryo.register(NegotiateRequest.class);
		kryo.register(Account.class);
		kryo.register(Tradeable.class);
		kryo.register(TradeRequest.class);
		kryo.register(Ack.class);
		kryo.register(LMSRBackend.class);
		kryo.register(Timestamp.class);
		kryo.register(Date.class);
		kryo.register(BidBundle.class);
		kryo.register(MarketState.class);
		kryo.register(SimpleBidBundle.class);
		kryo.register(BundleType.class);
		kryo.register(MarketOrder.class);
		kryo.register(TwoSidedAuction.class);
		kryo.register(ITwoSidedAuction.class);
		kryo.register(MechanismType.class);
		kryo.register(PaymentType.class);
		kryo.register(LMSR.class);
		kryo.register(ContinuousDoubleAuction.class);
		kryo.register(LMSRServer.class);
		kryo.register(ClearingRule.class);
		kryo.register(LMSRYesClearing.class);
		kryo.register(LMSRNoClearing.class);
		kryo.register(TradeableType.class);
		kryo.register(FullType.class);
		kryo.register(CDAServer.class);
		kryo.register(ClosestMatchClearing.class);
		kryo.register(LowestPriceClearing.class);
		kryo.register(Order.class);
		kryo.register(Ledger.class);
		kryo.register(HashMap.class);
		kryo.register(OpenOutcryRule.class);
		kryo.register(SealedBidRule.class);
		kryo.register(FirstPriceRule.class);
		kryo.register(SecondPriceRule.class);
		kryo.register(LiquiditySensitive.class);
		kryo.register(LukeMM.class);
		
		kryo.register(SimpleAuction.class);
		
		return true;
	}

}
