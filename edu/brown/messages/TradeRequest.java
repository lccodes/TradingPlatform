package brown.messages;

import java.util.List;

import brown.assets.accounting.Account;
import brown.assets.value.Share;

/*
 * A message sent to the server by an agent
 * when it wants to initiate a trade
 * note: -1 indicates offer to all agents
 */
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
	
	/*
	 * Overwrites the fromID field to prevent malicious
	 * offer creation
	 */
	public TradeRequest safeCopy(Integer correctID) {
		return new TradeRequest(toID, correctID, moniesRequested, 
				sharesRequested, moniesOffered, sharesOffered);
	}
	
	/*
	 * Method that determines if two agents' accounts satisfy
	 * the assets needed to execute this trade
	 */
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
