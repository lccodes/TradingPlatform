package brown.setup;

import java.util.ArrayList;

import brown.assets.Account;
import brown.assets.Share;
import brown.markets.PM;
import brown.markets.PredictionMarket;
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
		kryo.register(PM.class);
		kryo.register(PredictionMarket.class);
		kryo.register(Account.class);
		kryo.register(java.util.LinkedList.class);
		kryo.register(Share.class);
		kryo.register(MarketUpdate.class);
		kryo.register(ArrayList.class);
		kryo.register(Rejection.class);
		
		return true;
	}

}
