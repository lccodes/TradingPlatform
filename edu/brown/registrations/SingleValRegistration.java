package brown.registrations;

import brown.assets.value.BasicType;
import brown.messages.Registration;

/**
 * Registration with valuation of single good
 * 
 * @author lcamery
 */
public class SingleValRegistration extends Registration {
	/**
	 * Agent's valuation.
	 */
	private BasicType good;
	private Double value; 
	
	/**
	 * Empty Constructor for Kryo.
	 */
	public SingleValRegistration() {
		super(null);
		this.good = null;
		this.value = null; 
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param value
	 */
	public SingleValRegistration(Integer id, BasicType good, Double value) {
		super(id);
		this.good = good; 
		this.value = value;
	}

	/**
	 * Getter.
	 * 
	 * @return the agents valuation.
	 */
	public double getValue() {
		return value; 
	}

	public BasicType getGood() {
		return good;
	}
}
