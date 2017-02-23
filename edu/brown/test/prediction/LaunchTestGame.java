package brown.test.prediction;

import brown.exceptions.AgentCreationException;

public final class LaunchTestGame {
	public static void main(String[] args) throws AgentCreationException {
		TestServer serv = new TestServer(9922);
		
		if (args[0].equals("test")) {
			for (int i =0; i <3; i++) {
				new TestAgent("localhost", 9922);
			}
		}
		
		serv.startGame();
		while(true){}
	}
}
