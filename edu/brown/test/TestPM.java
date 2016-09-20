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
	public Share buyYes(Integer agentID, int shareNum) {
		if (shareNum <= 0) {
			return null;
		}
		
		Share testShares = new TestShare(agentID, shareNum);
		this.yes.add(testShares);
		return testShares;
	}

	@Override
	public Share buyNo(Integer agentID, int shareNum) {
		if (shareNum <= 0) {
			return null;
		}
		
		Share testShares = new TestShare(agentID, shareNum);
		this.no.add(testShares);
		return testShares;
	}

}
