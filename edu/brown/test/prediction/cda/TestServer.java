package brown.test.prediction.cda;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Account;
import brown.assets.value.FullType;
import brown.assets.value.Tradeable;
import brown.assets.value.TradeableType;
import brown.auctions.IMarket;
import brown.auctions.crules.ClosestMatchClearing;
import brown.exceptions.AgentCreationException;
import brown.messages.Registration;
import brown.securities.mechanisms.cda.CDAServer;
import brown.server.AgentServer;
import brown.setup.Logging;

import com.esotericsoftware.kryonet.Connection;

public class TestServer extends AgentServer {
	// TODO: Purely continuous!!!
	// TODO: MarketUpdate sends the ledger for past t timesteps

	private final int ID = 1;
	// private final FullType TYPENO = new FullType(SecurityType.PredictionNo,
	// ID);
	private final FullType TYPEYES = new FullType(TradeableType.PredictionYes, ID);
	// private boolean yes, no = false;

	private boolean C = Math.random() > .5;
	private boolean D = Math.random() > .5;

	public TestServer(int port) {
		super(port, new GameSetup());
	}

	@Override
	protected void onRegistration(Connection connection, Registration registration) {
		Integer theID = this.defaultRegistration(connection, registration);
		if (theID == null) {
			return;
		}

		this.theServer.sendToTCP(connection.getID(),
				new PMRegistration(theID, Math.random() > .5 ? C : D, (int) (Math.random() * 1000)));

		Account oldAccount = acctManager.getAccount(connections.get(connection));
		Tradeable newSec = new Tradeable(TYPEYES, 1.0, oldAccount.ID, state -> {
			List<Account> list = new LinkedList<Account>();
			if (state.STATE.getState() == 1) {
				list.add(new Account(null).add(state.QUANTITY * 100));
			}
			return list;
		});
		this.manager.register(1, newSec);
		// yes = true;
		// } /*else if (Math.random() < .5 && !no) {
		// newSec = new Security(oldAccount.ID, 1, TYPENO,
		// state -> state.getState() == 0 ? new Account(null).add(1) : null);
		// no = true;
		// }*/
		Account newAccount = oldAccount.add(50, newSec);
		acctManager.setAccount(connections.get(connection), newAccount);

		this.sendBankUpdate(connections.get(connection), oldAccount, newAccount);
	}

	private void delay(int amt, boolean update) {
		int i = 0;
		while (i < amt) {
			try {
				if (update) {
					for (IMarket market : this.manager.getAuctions()) {
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
		this.manager.open(new CDAServer(1, TYPEYES, new ClosestMatchClearing((Good g) -> {
			Contract newSec = new Contract(g.getAgentID(), g.getCount(), TYPEYES, state -> {
				List<Account> list = new LinkedList<Account>();
				if (state.STATE.getState() == 1) {
					list.add(new Account(null).add(100 * state.QUANTITY));
					list.add(new Account(g.getAgentID()).add(-100 * state.QUANTITY));
				}
				return list;
			});
			return newSec;
		})));
		Logging.log("[-] markets open");
		delay(1, false);

		Logging.log("[-] sent market updates");
		for (IMarket market : this.manager.getAuctions()) {
			this.sendMarketUpdate(market);
		}

		delay(3, true);

		TestState endState = new TestState(C);
		Logging.log("[-] markets close with " + C);
		// this.exchange.close(this, 0, endState);
		this.manager.close(this, 1, endState);
	}

	public void printBalances() {
		Logging.log("Bank Accounts: ");
		for (Integer privateID : this.privateToPublic.keySet()) {
			Logging.log("ID " + this.privateToPublic.get(privateID) + " (" + privateID + ") has $"
					+ this.acctManager.getAccount(privateID));
		}
	}

	public static void main(String[] args) throws AgentCreationException {
		TestServer serv = new TestServer(2121);

		for (int i = 0; i < 10; i++) {
			new LukeAgent("localhost", 2121);
		}

		serv.runGame();
		while (true) {
		}
	}

}
