package brown.tests;

import brown.agent.library.TestAgent;
import brown.exceptions.AgentCreationException;
import brown.server.library.TestServerLMSR;

public final class LaunchTestGame {
	public static void main(String[] args) throws AgentCreationException {
		TestServerLMSR serv = new TestServerLMSR(9922);
		
		if (args[0].equals("test")) {
			for (int i =0; i <3; i++) {
				new TestAgent("localhost", 9922);
			}
		}
		
		serv.startGame();
		while(true){}
	}
}
