package brown.lab;

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
  private final double value;

  /**
   * Empty Constructor for Kryo.
   */
  public ValuationRegistration() {
    super(null);
    this.value = -1;
  }

  /**
   * Constructor.
   * 
   * @param id
   * @param value
   */
  public ValuationRegistration(Integer id, double value) {
    super(id);
    this.value = value;
  }

  /**
   * Getter.
   * 
   * @return the agents valuation.
   */
  public double getValue() {
    return this.value;
  }
}
