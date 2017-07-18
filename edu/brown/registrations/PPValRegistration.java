package brown.registrations;

import java.util.Map;
import java.util.Set;

import brown.assets.value.FullType;
import brown.messages.Registration;
import brown.valuation.ValuationBundle;

/**
 * Registration with valuation from server for lab 3.
 * 
 * @author lcamery
 */
public class PPValRegistration extends Registration {
  /**
   * Agent's valuation.
   */
  private final ValuationBundle valueBundle;
  private final Set<FullType> allGoods;

  /**
   * Empty Constructor for Kryo.
   */
  public PPValRegistration() {
    super(null);
    this.valueBundle = null;
    this.allGoods = null; 
  }

  /**
   * Constructor.
   * 
   * @param id
   * @param value
   */
  public PPValRegistration(Integer id, ValuationBundle values, Set<FullType> allGoods) {
    super(id);
    this.valueBundle = values;
    this.allGoods = allGoods;
  }

  
  /**
   * Getter.
   * 
   * @return the agents valuation.
   */
  public double getValue(Set<FullType> type) {
    return this.valueBundle.getOrDefault(type, 0.0);
  }
  
  public Set<FullType> getGoods() {
    return this.allGoods;
  }

  public ValuationBundle getValues() {
    return this.valueBundle;
  }
}