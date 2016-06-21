package brown.setup;

import brown.messages.BankUpdate;
import brown.messages.Bid;
import brown.messages.BidRequest;
import brown.messages.PurchaseRequest;
import brown.messages.Registration;
import brown.messages.Trade;
import brown.messages.TradeRequest;

import com.esotericsoftware.kryo.Kryo;

public final class Startup {
	
	public static boolean start(Kryo kryo) {
		kryo.register(BankUpdate.class);
		kryo.register(Bid.class);
		kryo.register(BidRequest.class);
		kryo.register(PurchaseRequest.class);
		kryo.register(Registration.class);
		kryo.register(Trade.class);
		kryo.register(TradeRequest.class);
		
		return true;
	}

}
