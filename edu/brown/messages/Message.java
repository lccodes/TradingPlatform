package brown.messages;

public class Message {
	protected final Integer ID;
	
	public Message(Integer ID) {
		this.ID = ID;
	}
	
	public Integer getID() {
		return this.ID;
	}
}
