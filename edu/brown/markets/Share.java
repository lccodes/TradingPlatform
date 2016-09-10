package brown.markets;

import brown.agent.Agent;

/**
 * All non-cash assets will extend this class
 * Primarily for implementing stock or prediction markets
 */
public interface Share {
	Agent getAgent();
}
