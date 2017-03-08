package brown.lab.lab4;

import brown.assets.accounting.Ledger;
import brown.assets.accounting.Transaction;
import brown.assets.value.ITradeable;
import brown.exceptions.AgentCreationException;
import brown.lab.UnitCDAWrapper;
import brown.messages.BankUpdate;
import brown.messages.markets.GameReport;
import brown.setup.Logging;

public class Lab4DemoAgent extends Lab4Agent {
	private Ledger myLedger;

	public Lab4DemoAgent(String host, int port) throws AgentCreationException {
		super(host, port);
		this.myLedger = null;
	}

	@Override
	public void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("Current Bank: ");
		// print the money
		Logging.log("Monies = " + bankUpdate.newAccount.monies);
		// print the tradeables
		for (ITradeable t : bankUpdate.newAccount.tradeables) {
			Logging.log(t.toString());
		}
		// store the bank for future decision making
		this.myBalance = bankUpdate.newAccount;
	}

	@Override
	public void onMarketUpdate(GameReport marketUpdate) {
		this.myLedger = marketUpdate.LEDGER;
		// print the  transactions
		for (Transaction t : this.myLedger.getList()) {
			Logging.log("Tradeable " + t.TRADEABLE + " was sold to " + t.TO + " from " + t.FROM + " at price " + t.PRICE);
		}
	}

	@Override
	protected void onContinuousDoubleAuction(UnitCDAWrapper market) {
		//Print tradeabletype
		Logging.log("[-] tradeable in this market is " + market.getTradeable().getType());
		// Print buy orders
		for (Double price : market.getBuyBook().keySet()) {
			Logging.log("[-] " + market.getBuyBook().get(price) + " shares available at " + price);
		}
		// Random price to buy or sell
		int price = (int) (Math.random() * 100);
		// Buy with your coin
		if (this.myCoin) {
			// Buy at random price
			Logging.log("[-] buying at " + price);
			market.buy(this, price);
		} else {
			// Sell at random price
			Logging.log("[-] selling at " + price);
			market.sell(this, price);
		}

		//Randomly cancel a buy order
		if (Math.random() < .5) {
			market.cancel(this, true, 1);
		}
	}
}
