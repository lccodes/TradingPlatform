package brown.assets.value;

import brown.assets.accounting.Account;
import brown.securities.SecurityType;

/**
 * All non-cash assets will extend this class
 * Primarily for implementing stock or prediction markets
 */
public interface Tradeable {
	Integer getAgentID();
	void setAgentID(Integer ID);
	double getCount();
	void setCount(double count);
	SecurityType getType();
	
	Account close(State closingState);
	Tradeable split(double newCount);
}
