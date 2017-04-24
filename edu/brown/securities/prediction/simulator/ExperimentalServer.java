package brown.securities.prediction.simulator;

import brown.server.AgentServer;

public class ExperimentalServer extends AgentServer {

	public ExperimentalServer(int port) {
		super(port, new ExperimentalSetup());
	}

}
