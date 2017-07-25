package brown.registrations;

import java.util.Map;
import java.util.Set;

import brown.assets.value.BasicType;
import brown.messages.Registration;
import brown.valuation.ValuationBundle;

/**
 * Registration with valuation from server for lab 3.
 * 
 * @author lcamery
 */
public class ValuationRegistration extends Registration {
	/**
	 * Agent's valuation.
	 */
	private final ValuationBundle valueBundle;

	/**
	 * Empty Constructor for Kryo.
	 */
	public ValuationRegistration() {
		super(null);
		this.valueBundle = null;
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param value
	 */
	public ValuationRegistration(Integer id, ValuationBundle values) {
		super(id);
		this.valueBundle = values;
	}

	
	/**
	 * Getter.
	 * 
	 * @return the agents valuation.
	 */
	public double getValue(Set<BasicType> type) {
		return this.valueBundle.getOrDefault(type, 0.0);
	}

	public ValuationBundle getValues() {
		return this.valueBundle;
	}
}
