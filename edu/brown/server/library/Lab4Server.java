package brown.server.library;

import java.util.LinkedList;
import java.util.List;

import brown.agent.library.Lab4DemoAgent;
import brown.assets.accounting.Account;
import brown.assets.value.FullType;
import brown.assets.value.TradeableType;
import brown.clearingrules.ClosestMatchClearing;
import brown.exceptions.AgentCreationException;
import brown.markets.CDAServer;
import brown.markets.IMarket;
import brown.messages.Registration;
import brown.registrations.PMRegistration;
import brown.server.AgentServer;
import brown.setup.Logging;
import brown.setup.library.CDAGameSetup;
import ch.uzh.ifi.ce.mweiss.specval.model.Good;

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

	public Lab4Server(int port) {
		super(port, new CDAGameSetup());
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
	
	//registers a bunch of decoys? AC
	public void register(Connection conn) {
		int decoys = (int) Math.ceil((Math.random() * (this.connections.size()/2.0)));
		decoys = decoys == 0 ? 1 : decoys;
		this.theServer.sendToTCP(conn.getID(), new PMRegistration(this.connections.get(conn),
				Math.random() <= 1/(decoys+1) ? C : Math.random() > .5, decoys));
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
	
	public void printBalances() {
		Logging.log("Bank Accounts: ");
		for (Integer privateID : this.privateToPublic.keySet()) {
			if (privateID == -1) {
				continue;
			}
			Logging.log("ID " + this.privateToPublic.get(privateID) + " (" + privateID + ") has $"
					+ this.acctManager.getAccount(privateID).monies);
		}
	}

	public void runGame() {
		delay(2, false);
		this.closed = true;
		for (Connection c : this.connections.keySet()) {
			register(c);
		}
		// this.exchange.open(new ContinuousDoubleAuction(0, TYPENO, new
		// ClosestMatchClearing()));
		this.manager.open(new CDAServer(1, TYPEYES,
				new ClosestMatchClearing((Good g) -> {
					Contract newSec = new Contract(g.getAgentID(), g.getCount(), TYPEYES,
					(Contract.EndState state) -> {
						List<Account> list = new LinkedList<Account>();
						if(state.STATE.getState() == 1) {
							list.add(new Account(null).add(100*state.QUANTITY));
							list.add(new Account(g.getAgentID()).add(-100*state.QUANTITY));
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

		TestStateCDA endState = new TestStateCDA(C);
		Logging.log("[-] markets close with " + C);
		// this.exchange.close(this, 0, endState);
		this.manager.close(this, 1, endState);
		printBalances();
	}

	public static void main(String[] args) throws AgentCreationException {
		Lab4Server serv = new Lab4Server(2121);

		for (int i = 0; i < 5; i++) {
			new Lab4DemoAgent("localhost", 2121);
		}

		serv.runGame();
		while (true) {
		}
	}

}
