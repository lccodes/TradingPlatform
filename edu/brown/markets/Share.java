package brown.markets;

/**
 * All non-cash assets will extend this class
 * Primarily for implementing stock or prediction markets
 */
public interface Share {
	Integer getAgentPublicId();
	int getCount();
}
