package brown.server.library;

import java.util.Map.Entry;

import com.esotericsoftware.kryonet.Connection;

import brown.assets.accounting.Account;
import brown.assets.accounting.MarketManager;
import brown.auctions.IMarketServer;
import brown.messages.markets.MarketOrder;
import brown.messages.markets.TradeRequest;
import brown.securities.prediction.simulator.ExperimentalSetup;
import brown.server.AgentServer;
import brown.setup.Logging;

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
	public void sendMarketUpdateNL(IMarketServer market) {
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
			this.acctManager.setAccount(ID, a);
			this.sendBankUpdate(ID, new Account(ID), a);
		}
	}
	
	@Override
	public void onLimitOrder(Connection connection, Integer privateID,
			MarketOrder limitorder) {
		if (limitorder.buyShares != 0 || limitorder.sellShares != 0) {
			//System.out.println("buy-sell " + limitorder.buyShares + "-" + limitorder.sellShares);
			super.onLimitOrder(connection, privateID, limitorder);
		}
		
		this.sendMarketUpdateNL(this.manager.getTwoSided(1));
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			Logging.log("[+] woken: " + e.getMessage());
		}
		this.sendMarketUpdateNL(this.manager.getTwoSided(0));
	}

}
