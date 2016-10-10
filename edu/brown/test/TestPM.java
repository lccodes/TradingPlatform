package brown.test;

import brown.assets.accounting.Transaction;
import brown.securities.prediction.PM;

public class TestPM extends PM {
	
	public TestPM(Integer ID, double b) {
		super(ID, b);
	}
	
	public TestPM() {
		super(null, 0);
	}

	@Override
	public Integer getID() {
		return this.ID;
	}

	@Override
	public Transaction buy(Integer agentID, int shareNum) {
		Transaction trans = new Transaction(this, shareNum, agentID, cost(shareNum, 0));
		this.yes++;
		return trans;
	}

	@Override
	public Transaction sell(Integer agentID, int shareNum) {
		Transaction trans = new Transaction(this, shareNum, agentID, cost(0, shareNum));
		this.no++;
		return trans;
	}

}
