package brown.lab.finalproject;

import ch.uzh.ifi.ce.mweiss.specval.model.UnsupportedBiddingLanguageException;
import brown.exceptions.AgentCreationException;

public class FinalProjectTests {
	public static void main(String[] args) {
		FinalProjectServer l3s = new FinalProjectServer(2121);
		Thread mainLoop = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					l3s.runGame(true);
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
						new FinalProjectDemo("localhost", 2121);
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
