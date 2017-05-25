package brown.exceptions;

/**
 * Any failure in the construction of an agent
 * will throw this uncatched exception
 */
public class AgentCreationException extends Exception {

	private static final long serialVersionUID = -9089084118116290951L;
	
	/**
	 * Message for when the agent fails to init
	 * @param message
	 */
	public AgentCreationException(String message) {
		super(message);
	}

}
