package brown.messages;

public class Message {
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
}
