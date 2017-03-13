package brown.lab.lab5;

import brown.exceptions.AgentCreationException;
import brown.messages.BankUpdate;
import brown.setup.Logging;

public class Lab5Example extends Lab5Agent {

	public Lab5Example(String host, int port) throws AgentCreationException {
		super(host, port);
	}

	@Override
	public void onLemonadeUpdate(LemonadeReport lemonadeReport) {
		Logging.log("My slot " + lemonadeReport.getCount(0));
	}

	@Override
	public void onLemonade(LemonadeWrapper wrapper) {
		Logging.log("My slot is always 0");
		wrapper.pickPosition(this, 0);
	}

	@Override
	public void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("Watch my utils grow: " + bankUpdate.newAccount.monies);
	}

}
