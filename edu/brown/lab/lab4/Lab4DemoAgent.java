package brown.lab.lab4;

import brown.assets.accounting.Ledger;
import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.messages.markets.MarketUpdate;
import brown.securities.mechanisms.cda.CDAWrapper;
import brown.setup.Logging;

public class Lab4DemoAgent extends Lab4Agent {
	private Ledger myLedger;

	public Lab4DemoAgent(String host, int port) throws AgentCreationException {
		super(host, port);
		this.myLedger = null;
	}

	@Override
	public void onContinuousDoubleAuction(CDAWrapper market) {
		// Random price to buy or sell
		double price = Math.random() * 100;
		// Buy with your coin
		if (this.myCoin) {
			// Buy at random price
			Logging.log("[-] buying at " + price);
			market.buy(this, 1, price);
		} else {
			Logging.log("[-] selling at " + price);
			market.buy(this, 1, price);
		}
	}

	@Override
	public void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("Current Bank: ");
		Logging.log("Monies = " + bankUpdate.newAccount.monies);
		// print the rest
		this.myBalance = bankUpdate.newAccount;
	}

	@Override
	public void onMarketUpdate(MarketUpdate marketUpdate) {
		this.myLedger = marketUpdate.LEDGER;
		// show transactions !!
		Logging.log("New ledger: " + this.myLedger);
	}
}
