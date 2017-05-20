package brown.lab;

import brown.lab.finalproject.FinalProjectGood;
import brown.lab.lab5.LemonadeReport;
import brown.lab.lab8.Lab8Good;
import brown.setup.Setup;

import com.esotericsoftware.kryo.Kryo;

/**
 * Sets the classes to be used in lab 3.
 * 
 * @author lcamery
 */
public class GameSetup implements Setup{

  @Override
  public void setup(Kryo kryo) {
    kryo.register(Lab8Good.class);
    kryo.register(ValuationRegistration.class);
    kryo.register(LemonadeReport.class);
    kryo.register(FinalProjectGood.class);
    //kryo.register(LemonadeWrapper.class);
  }

}
