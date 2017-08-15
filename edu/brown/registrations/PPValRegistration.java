package brown.registrations;

import java.util.Map;
import java.util.Set;

import brown.assets.value.BasicType;
import brown.messages.Registration;
import brown.valuable.IValuable;
import brown.valuation.IValuationSet;

/**
 * Registration with valuation from server for lab 3.
 * 
 * @author lcamery
 */
public class PPValRegistration extends Registration {
  /**
   * Agent's valuation.
   */
  private final Map<Set<BasicType>, Double> valueBundle;
  private final Set<BasicType> allGoods;

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
  public PPValRegistration(Integer id, Map<Set<BasicType>, Double> values, Set<BasicType> allGoods) {
    super(id);
    this.valueBundle = values;
    this.allGoods = allGoods;
  }

  
  /**
   * Getter.
   * 
   * @return the agents valuation.
   */
  public double getValue(Set<BasicType> type) {
    return this.valueBundle.getOrDefault(type, 0.0);
  }
  
  public Set<BasicType> getGoods() {
    return this.allGoods;
  }

  public Map<Set<BasicType>, Double> getValues() {
    return this.valueBundle;
  }
}