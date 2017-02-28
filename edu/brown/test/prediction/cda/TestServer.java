package brown.test.prediction.cda;

import brown.assets.accounting.Account;
import brown.assets.value.FullType;
import brown.assets.value.Security;
import brown.assets.value.SecurityType;
import brown.auctions.crules.ClosestMatchClearing;
import brown.auctions.twosided.TwoSidedAuction;
import brown.exceptions.AgentCreationException;
import brown.messages.Registration;
import brown.securities.mechanisms.cda.ContinuousDoubleAuction;
import brown.server.AgentServer;
import brown.setup.Logging;

import com.esotericsoftware.kryonet.Connection;

public class TestServer extends AgentServer {
	private final int ID = 1;
	// private final FullType TYPENO = new FullType(SecurityType.PredictionNo,
	// ID);
	private final FullType TYPEYES = new FullType(SecurityType.PredictionYes,
			ID);
	// private boolean yes, no = false;

	private boolean C = Math.random() > .5;
	private boolean D = Math.random() > .5;

	public TestServer(int port) {
		super(port, new GameSetup());
	}

	@Override
	protected void onRegistration(Connection connection,
			Registration registration) {
		Integer theID = this.defaultRegistration(connection, registration);
		if (theID == null) {
			return;
		}

		this.theServer.sendToTCP(connection.getID(), new PMRegistration(theID,
				Math.random() > .5 ? C : D));

		Account oldAccount = bank.get(connections.get(connection));
		Security newSec = new Security(oldAccount.ID, 1, TYPEYES,
				state -> state.getState() == 1 ? new Account(null).add(100)
						: null);
		newSec.setClosure(state -> state.getState() == 1 ? new Account(null)
				.add(newSec.getCount() * 100) : null);
		this.exchange.register(1, newSec);
		// yes = true;
		// } /*else if (Math.random() < .5 && !no) {
		// newSec = new Security(oldAccount.ID, 1, TYPENO,
		// state -> state.getState() == 0 ? new Account(null).add(1) : null);
		// no = true;
		// }*/
		Account newAccount = oldAccount.add(50, newSec);
		bank.put(connections.get(connection), newAccount);

		this.sendBankUpdate(connections.get(connection), oldAccount, newAccount);
	}

	private void delay(int amt, boolean update) {
		int i = 0;
		while (i < amt) {
			try {
				if (update) {
					for (TwoSidedAuction market : this.exchange.getAuctions()) {
						this.sendMarketUpdate(market);
					}
				}
				Thread.sleep(1000);
				Logging.log("[-] pause phase " + i++);
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		}
	}

	public void runGame() {
		// this.exchange.open(new ContinuousDoubleAuction(0, TYPENO, new
		// ClosestMatchClearing()));
		this.exchange.open(new ContinuousDoubleAuction(1, TYPEYES,
				new ClosestMatchClearing()));
		Logging.log("[-] markets open");
		delay(1, false);

		Logging.log("[-] sent market updates");
		for (TwoSidedAuction market : this.exchange.getAuctions()) {
			this.sendMarketUpdate(market);
		}

		delay(3, true);

		TestState endState = new TestState(C);
		Logging.log("[-] markets close with " + C);
		// this.exchange.close(this, 0, endState);
		this.exchange.close(this, 1, endState);
	}

	public static void main(String[] args) throws AgentCreationException {
		TestServer serv = new TestServer(2121);

		//for (int i = 0; i < 50; i++) {
		//	new LukeAgent("localhost", 2121);
		//}

		serv.runGame();
		while (true) {
		}
	}

}
