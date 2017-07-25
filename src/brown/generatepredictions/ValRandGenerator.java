
package brown.generatepredictions;

import java.util.Set;

import brown.interfaces.IValuationGenerator;

public class ValRandGenerator implements IValuationGenerator {

  @Override
  public double makeValuation(Good good) {
    return (Math.random()*Constants.MAX_VAL +Constants.MIN_VAL);
  }

  @Override
  public double makeValuation(Set<Good> goods) {
    return (Math.random()*Constants.MAX_VAL + Constants.MIN_VAL);
  }

}