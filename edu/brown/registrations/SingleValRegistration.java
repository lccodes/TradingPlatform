package brown.registrations;

import java.util.Map;
import java.util.Set;

import brown.assets.value.FullType;
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
	private final Map<FullType, Double> valueMap;

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
	public SingleValRegistration(Integer id, Map<FullType, Double> values) {
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
