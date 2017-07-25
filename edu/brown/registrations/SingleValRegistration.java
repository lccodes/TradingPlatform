package brown.registrations;

import java.util.Map;
import java.util.Set;

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
	private final Map<BasicType, Double> valueMap;

	/**
	 * Empty Constructor for Kryo.
	 */
	public SingleValRegistration() {
		super(null);
		this.valueMap = null;
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param value
	 */
	public SingleValRegistration(Integer id, Map<BasicType, Double> values) {
		super(id);
		this.valueMap = values;
	}

	/**
	 * Getter.
	 * 
	 * @return the agents valuation.
	 */
	public double getValue(BasicType type) {
		return this.valueMap.getOrDefault(type, 0.0);
	}

	public Map<BasicType, Double> getValues() {
		return this.valueMap;
	}
}
