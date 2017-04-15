package brown.lab.lab3;

import brown.exceptions.AgentCreationException;

public class Lab3Tests {
	public static void main(String[] args) {
		Lab3Server l3s = new Lab3Server(2121);
		Thread mainLoop = new Thread(new Runnable() {

			@Override
			public void run() {
				l3s.runGame(false, true, 0);
			}
			
		});
		mainLoop.start();
		for (int i = 0; i < 5; i++) {
			Thread otherThread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						new Lab3Demo("localhost", 2121);
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
