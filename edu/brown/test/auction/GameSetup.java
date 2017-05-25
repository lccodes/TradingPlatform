package brown.test.auction;

import brown.setup.Setup;

import com.esotericsoftware.kryo.Kryo;

public final class GameSetup implements Setup {
  
  public void setup(Kryo kryo) {
    kryo.register(TheGood.class);
    kryo.register(AuctionRegistration.class);
  }

}
