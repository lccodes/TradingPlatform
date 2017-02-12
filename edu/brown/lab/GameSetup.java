package brown.lab;

import brown.lab.lab3.Lab3Good;
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
    kryo.register(Lab3Good.class);
    kryo.register(ValuationRegistration.class);
  }

}
