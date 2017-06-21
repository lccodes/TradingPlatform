package brown.agent.library;

import brown.agent.Agent;
import brown.exceptions.AgentCreationException;
import brown.markets.Lemonade;
import brown.setup.library.LabGameSetup;

public abstract class LabAgent extends Agent {

	public LabAgent(String host, int port)
			throws AgentCreationException {
		super(host, port, new LabGameSetup());
	}
	
	public abstract void onLemonade(Lemonade wrapper);

}
