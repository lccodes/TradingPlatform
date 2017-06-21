package brown.setup.library;

import brown.messages.markets.LemonadeReport;
import brown.registrations.ValuationRegistration;
import brown.setup.Setup;
import brown.tradeables.FinalProjectGood;
import brown.tradeables.Lab8Good;

import com.esotericsoftware.kryo.Kryo;

/**
 * Sets the classes to be used in lab 3.
 * 
 * @author lcamery
 */
public class LabGameSetup implements Setup{

  @Override
  public void setup(Kryo kryo) {
    kryo.register(Lab8Good.class);
    kryo.register(ValuationRegistration.class);
    kryo.register(LemonadeReport.class);
    kryo.register(FinalProjectGood.class);
    //kryo.register(LemonadeWrapper.class);
  }

}
