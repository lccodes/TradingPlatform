package brown.lab.lab4;

import brown.assets.accounting.Account;
import brown.assets.value.Contract;
import brown.assets.value.FullType;
import brown.assets.value.TradeableType;
import brown.auctions.IMarket;
import brown.auctions.crules.ClosestMatchClearing;
import brown.exceptions.AgentCreationException;
import brown.messages.Registration;
import brown.securities.mechanisms.cda.ContinuousDoubleAuction;
import brown.server.AgentServer;
import brown.setup.Logging;
import brown.test.prediction.cda.GameSetup;
import brown.test.prediction.cda.PMRegistration;
import brown.test.prediction.cda.TestState;

import com.esotericsoftware.kryonet.Connection;

public class Lab4Server extends AgentServer {
	//TODO: Purely continuous!!!
	//TODO: MakretUpdate sends the ledger for past t timesteps
	private boolean closed = false;
	
	private final int ID = 1;
	// private final FullType TYPENO = new FullType(SecurityType.PredictionNo,
	// ID);
	private final FullType TYPEYES = new FullType(TradeableType.PredictionYes,
			ID);
	// private boolean yes, no = false;

	private boolean C = Math.random() > .5;
	private boolean D = Math.random() > .5;

	public Lab4Server(int port) {
		super(port, new GameSetup());
	}

	@Override
	protected void onRegistration(Connection connection,
			Registration registration) {
		if (closed) {
			return;
		}
		Integer theID = this.defaultRegistration(connection, registration);
		if (theID == null) {
			return;
		}

		/*Account oldAccount = bank.get(connections.get(connection));
		Contract newSec = new Contract(oldAccount.ID, 1, TYPEYES,
				state -> state.getState() == 1 ? new Account(null).add(100)
						: null);
		newSec.setClosure(state -> state.getState() == 1 ? new Account(null)
				.add(newSec.getCount() * 100) : null);
		this.exchange.register(1, newSec);*/
		// yes = true;
		// } /*else if (Math.random() < .5 && !no) {
		// newSec = new Security(oldAccount.ID, 1, TYPENO,
		// state -> state.getState() == 0 ? new Account(null).add(1) : null);
		// no = true;
		// }*/
		//Account newAccount = oldAccount.add(50, newSec);
		//bank.put(connections.get(connection), newAccount);

		//this.sendBankUpdate(connections.get(connection), oldAccount, newAccount);
	}
	
	public void register(Connection conn) {
		int decoys = (int) (Math.random() * Math.pow(2, this.connections.size()));
		this.theServer.sendToTCP(conn.getID(), new PMRegistration(this.connections.get(conn),
				Math.random() <= 1/(decoys+1) ? C : D, decoys));
	}

	private void delay(int amt, boolean update) {
		int i = 0;
		while (i < amt) {
			try {
				if (update) {
					for (IMarket market : this.exchange.getAuctions()) {
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
		delay(10, false);
		this.closed = true;
		for (Connection c : this.connections.keySet()) {
			register(c);
		}
		// this.exchange.open(new ContinuousDoubleAuction(0, TYPENO, new
		// ClosestMatchClearing()));
		this.exchange.open(new ContinuousDoubleAuction(1, TYPEYES,
				new ClosestMatchClearing((Double d) -> {
					Contract newSec = new Contract(null, 1, TYPEYES,
							state -> state.getState() == 1 ? new Account(null).add(100)
									: null);
					newSec.setClosure(state -> state.getState() == 1 ? new Account(null)
							.add(newSec.getCount() * 100) : null);
					return newSec;
				})));
		Logging.log("[-] markets open");
		delay(1, false);

		Logging.log("[-] sent market updates");
		for (IMarket market : this.exchange.getAuctions()) {
			this.sendMarketUpdate(market);
		}

		delay(3, true);

		TestState endState = new TestState(C);
		Logging.log("[-] markets close with " + C);
		// this.exchange.close(this, 0, endState);
		this.exchange.close(this, 1, endState);
	}

	public static void main(String[] args) throws AgentCreationException {
		Lab4Server serv = new Lab4Server(2121);

		for (int i = 0; i < 50; i++) {
			new Lab4DemoAgent("localhost", 2121);
		}

		serv.runGame();
		while (true) {
		}
	}

}
