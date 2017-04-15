package brown.lab;

import java.util.Map;

import brown.assets.value.FullType;
import brown.messages.Registration;

/**
 * Registration with valuation from server for lab 3.
 * 
 * @author lcamery
 */
public class ValuationRegistration extends Registration {
  /**
   * Agent's valuation.
   */
  private final Map<FullType, Double> valueMap;

  /**
   * Empty Constructor for Kryo.
   */
  public ValuationRegistration() {
    super(null);
    this.valueMap = null;
  }

  /**
   * Constructor.
   * 
   * @param id
   * @param value
   */
  public ValuationRegistration(Integer id, Map<FullType, Double> values) {
    super(id);
    this.valueMap = values;
  }

  /**
   * Getter.
   * 
   * @return the agents valuation.
   */
  public double getValue(FullType type) {
    return this.valueMap.getOrDefault(type, 0.0);
  }

public Map<FullType, Double> getValues() {
	return this.valueMap;
}
}
