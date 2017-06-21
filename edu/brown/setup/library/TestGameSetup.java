package brown.setup.library;

import brown.registrations.AuctionRegistration;
import brown.setup.Setup;
import brown.tradeables.TheGood;

import com.esotericsoftware.kryo.Kryo;

public final class TestGameSetup implements Setup {
  
  public void setup(Kryo kryo) {
    kryo.register(TheGood.class);
    kryo.register(AuctionRegistration.class);
  }

}
