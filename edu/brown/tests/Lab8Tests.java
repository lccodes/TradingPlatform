package brown.tests;

import ch.uzh.ifi.ce.mweiss.specval.model.UnsupportedBiddingLanguageException;
import brown.agent.library.Lab8Demo;
import brown.exceptions.AgentCreationException;
import brown.server.library.Lab8Server;

public class Lab8Tests {
	public static void main(String[] args) {
		Lab8Server l3s = new Lab8Server(2121);
		Thread mainLoop = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					l3s.runGame(true, true, 0);
				} catch (UnsupportedBiddingLanguageException e) {
					e.printStackTrace();
				}
			}
			
		});
		mainLoop.start();
		for (int i = 0; i < 6; i++) {
			Thread otherThread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						new Lab8Demo("localhost", 2121);
					} catch (AgentCreationException e) {
						e.printStackTrace();
						return;
					}
					while(true){}
				}
				
			});
			otherThread.start();
		}
	}
}
