package brown.assets.value;

import brown.assets.accounting.Account;

/**
 * All non-cash assets will extend this class
 * Primarily for implementing stock or prediction markets
 */
public interface ITradeable {
	Integer getAgentID();
	void setAgentID(Integer ID);
	double getCount();
	void setCount(double count);
	FullType getType();
	
	Account close(StateOfTheWorld closingState);
	ITradeable split(double newCount);
	ITradeable toAgent();
}
