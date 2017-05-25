package brown.lab;

import brown.agent.Agent;
import brown.exceptions.AgentCreationException;
import brown.lab.lab5.Lemonade;

public abstract class LabAgent extends Agent {

	public LabAgent(String host, int port)
			throws AgentCreationException {
		super(host, port, new GameSetup());
	}
	
	public abstract void onLemonade(Lemonade wrapper);

}
