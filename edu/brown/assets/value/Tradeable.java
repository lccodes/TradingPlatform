package brown.assets.value;

/**
 * All non-cash assets will extend this class
 * Primarily for implementing stock or prediction markets
 */
public interface Tradeable {
	Integer getAgentID();
	void setAgentID(Integer ID);
	double getCount();
}
