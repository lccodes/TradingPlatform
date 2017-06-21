package brown.agent.library;

import brown.exceptions.AgentCreationException;
import brown.markets.Lemonade;
import brown.markets.SimpleAuction;
import brown.messages.BankUpdate;
import brown.messages.markets.LemonadeReport;
import brown.setup.Logging;

public class Lab5Example extends Lab5Agent {
	private final int mySlot = (int) (Math.random() * 11) + 1;

	public Lab5Example(String host, int port) throws AgentCreationException {
		super(host, port);
	}

	@Override
	public void onLemonadeUpdate(LemonadeReport lemonadeReport) {
		Logging.log("Number of people at my slot " + lemonadeReport.getCount(mySlot));
	}

	@Override
	public void onLemonade(Lemonade wrapper) {
		Logging.log("My slot is always " + mySlot);
		wrapper.pickPosition(this, mySlot);
	}

	@Override
	public void onBankUpdate(BankUpdate bankUpdate) {
		Logging.log("Watch my utils grow: " + bankUpdate.newAccount.monies);
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new Lab5Example("localhost", 2121);
		while(true){}
	}

	@Override
	public void onSimpleSealed(SimpleAuction simpleWrapper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSimpleOpenOutcry(SimpleAuction simpleWrapper) {
		// TODO Auto-generated method stub
		
	}

}
