package brown.securities.prediction.simulator;

import java.util.Map.Entry;

import com.esotericsoftware.kryonet.Connection;

import brown.assets.accounting.Account;
import brown.assets.accounting.MarketManager;
import brown.auctions.IMarket;
import brown.messages.markets.TradeRequest;
import brown.server.AgentServer;

public class ExperimentalServer extends AgentServer {

	public ExperimentalServer(int port) {
		super(port, new ExperimentalSetup());
	}
	
	public MarketManager getManager() {
		return this.manager;
	}
	
	/*
	 * Sends a MarketUpdate about this specific market to all agents
	 * without the ledger
	 * @param Security : the market to update on
	 */
	public void sendMarketUpdateNL(IMarket market) {
		synchronized(market) {
			for (Entry<Connection, Integer> ID : this.connections.entrySet()) {
				TradeRequest mupdate = new TradeRequest(0, market.wrap(null),
						market.getMechanismType());
				theServer.sendToTCP(ID.getKey().getID(), mupdate);
			}
		}
	}
	
	public void setBanks(double amount) {
		for (Integer ID : this.connections.values()) {
			Account a = new Account(ID).add(amount);
			this.setAccount(ID, a);
			this.sendBankUpdate(ID, null, a);
		}
	}

}
