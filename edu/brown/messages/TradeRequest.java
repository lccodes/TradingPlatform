package brown.messages;

import java.util.List;

import brown.server.Account;
import brown.server.Share;

public class TradeRequest {
	public final Integer toID;
	public final Integer fromID;
	
	public final Integer moniesRequested;
	public final List<Share> sharesRequested;
	
	public final Integer moniesOffered;
	public final List<Share> sharesOffered;
	
	public TradeRequest(Integer toID, Integer fromID, Integer moniesRequested, 
			List<Share> sharesRequested, Integer moniesOffered, List<Share> sharesOffered) {
		this.toID = toID;
		this.fromID = fromID;
		
		this.moniesRequested = moniesRequested;
		this.moniesOffered = moniesOffered;
		
		this.sharesOffered = sharesOffered;
		this.sharesRequested = sharesRequested;
	}
	
	public TradeRequest safeCopy(Integer correctID) {
		return new TradeRequest(toID, correctID, moniesRequested, 
				sharesRequested, moniesOffered, sharesOffered);
	}
	
	public boolean isSatisfied(Account toAccount, Account fromAccount) {
		if (fromAccount.monies < moniesOffered || !fromAccount.shares.containsAll(sharesOffered)) {
			return false;
		}
		
		if (toAccount.monies < moniesRequested || !toAccount.shares.containsAll(sharesRequested)) {
			return false;
		}
		
		return true;
	}

}
