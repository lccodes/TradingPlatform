package brown.test.auction;

import com.esotericsoftware.kryo.Kryo;

public final class GameSetup {
  
  public static void setup(Kryo kryo) {
    kryo.register(TheGood.class);
    kryo.register(AuctionRegistration.class);
  }

}
