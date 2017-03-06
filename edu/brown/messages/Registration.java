package brown.messages;

import brown.agent.Agent;

public class Registration extends Message {
	
	/**
	 * Kronet requires an empty constructor
	 * DO NOT USE
	 */
	public Registration() {
		super(null);
	}

	/**
	 * Registration when an agent connects to the server
	 * Server sends back with the agent's ID
	 * @param ID : agent's ID
	 */
	public Registration(Integer ID) {
		super(ID);
	}

	@Override
	public void dispatch(Agent agent) {
		agent.onRegistration(this);
	}
}
