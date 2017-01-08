package brown.setup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import brown.assets.accounting.Account;
import brown.assets.accounting.Transaction;
import brown.assets.value.Good;
import brown.auctions.BidBundle;
import brown.auctions.BundleType;
import brown.auctions.bundles.SimpleBidBundle;
import brown.messages.BankUpdate;
import brown.messages.Message;
import brown.messages.Registration;
import brown.messages.Rejection;
import brown.messages.auctions.Bid;
import brown.messages.auctions.BidRequest;
import brown.messages.markets.MarketUpdate;
import brown.messages.markets.PurchaseRequest;
import brown.messages.trades.Trade;
import brown.messages.trades.TradeRequest;
import brown.securities.Security;
import brown.securities.SecurityWrapper;
import brown.securities.prediction.PredictionMarket;
import brown.securities.prediction.structures.PMBackend;
import brown.securities.prediction.structures.PMNo;
import brown.securities.prediction.structures.PMYes;

import com.esotericsoftware.kryo.Kryo;

public final class Startup {
	
	//TODO: Consider reflection for dynamic loading
	public static boolean start(Kryo kryo) {
		kryo.register(Message.class);
		kryo.register(BankUpdate.class);
		kryo.register(Bid.class);
		kryo.register(BidRequest.class);
		kryo.register(PurchaseRequest.class);
		kryo.register(Registration.class);
		kryo.register(Trade.class);
		kryo.register(TradeRequest.class);
		kryo.register(Security.class);
		kryo.register(PredictionMarket.class);
		kryo.register(Account.class);
		kryo.register(java.util.LinkedList.class);
		kryo.register(Good.class);
		kryo.register(MarketUpdate.class);
		kryo.register(ArrayList.class);
		kryo.register(Rejection.class);
		kryo.register(SecurityWrapper.class);
		kryo.register(PMYes.class);
		kryo.register(Transaction.class);
		kryo.register(PMBackend.class);
		kryo.register(PMNo.class);
		kryo.register(Timestamp.class);
		kryo.register(Date.class);
		kryo.register(BidBundle.class);
		kryo.register(SimpleBidBundle.class);
		kryo.register(BundleType.class);
		
		return true;
	}

}
