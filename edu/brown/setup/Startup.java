package brown.setup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import brown.assets.accounting.Account;
import brown.assets.accounting.Transaction;
import brown.assets.value.Share;
import brown.messages.BankUpdate;
import brown.messages.Bid;
import brown.messages.BidRequest;
import brown.messages.MarketUpdate;
import brown.messages.Message;
import brown.messages.PurchaseRequest;
import brown.messages.Registration;
import brown.messages.Rejection;
import brown.messages.Trade;
import brown.messages.TradeRequest;
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
		kryo.register(Share.class);
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
		
		return true;
	}

}
