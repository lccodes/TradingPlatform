package brown.lab.lab4;

import brown.assets.accounting.Ledger;
import brown.assets.accounting.Transaction;
import brown.assets.value.ITradeable;
import brown.exceptions.AgentCreationException;
import brown.lab.UnitCDAWrapper;
import brown.messages.BankUpdate;
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
	protected void onContinuousDoubleAuction(UnitCDAWrapper market) {
		Logging.log("My decoys " + this.myNumDecoys);
		// check out the current ledger
		this.myLedger = market.getLedger();
		// print the  transactions
		for (Transaction t : this.myLedger.getList()) {
			Logging.log("Tradeable " + t.TRADEABLE + " was sold to " + t.TO + " from " + t.FROM + " at price " + t.PRICE);
		}
		
		//Print tradeabletype
		Logging.log("[-] tradeable in this market is " + market.getTradeableType());
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

		//Randomly cancel a buy order if you have less than two decoy coins
		if (Math.random() < .5 && this.myNumDecoys < 2) {
			market.cancel(this, true, 1);
		}
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new Lab4DemoAgent("localhost", 2121);
		while(true){}
	}
}
