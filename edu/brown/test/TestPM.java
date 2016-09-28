package brown.test;

import brown.assets.Share;
import brown.markets.PM;

public class TestPM extends PM {
	
	public TestPM() {
		super(0, 0);
	}

	public TestPM(Integer id, double b) {
		super(id, b);
	}
	
	@Override
	public Share buyPositive(Integer agentID, int shareNum) {
		if (shareNum <= 0) {
			return null;
		}
		
		Share testShares = new TestShare(agentID, shareNum);
		this.yes.add(testShares);
		return testShares;
	}

	@Override
	public Share buyNegative(Integer agentID, int shareNum) {
		if (shareNum <= 0) {
			return null;
		}
		
		Share testShares = new TestShare(agentID, shareNum);
		this.no.add(testShares);
		return testShares;
	}

	@Override
	public void sellPositive(Integer agentID, int shareNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sellNegative(Integer agentID, int shareNum) {
		// TODO Auto-generated method stub
		
	}

}
