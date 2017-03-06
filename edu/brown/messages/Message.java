package brown.messages;

import brown.agent.Agent;

public abstract class Message {
	protected final Integer ID;
	
	/**
	 * Empty message
	 * @param ID : message ID
	 */
	public Message(Integer ID) {
		this.ID = ID;
	}
	
	/**
	 * Get message ID
	 * @return ID
	 */
	public Integer getID() {
		return this.ID;
	}
	
	/**
	 * Tells agent what type of message we are
	 * @param agent
	 */
	public abstract void dispatch(Agent agent);
}
