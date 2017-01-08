package brown.messages.trades;

import java.util.List;

import brown.assets.accounting.Account;
import brown.assets.value.Good;

/*
 * A message sent to the server by an agent
 * when it wants to initiate a trade
 * note: -1 indicates offer to all agents
 */
public class TradeRequest {
	public final Integer toID;
	public final Integer fromID;
	
	public final Integer moniesRequested;
	public final List<Good> sharesRequested;
	
	public final Integer moniesOffered;
	public final List<Good> sharesOffered;
	
	public TradeRequest(Integer toID, Integer fromID, Integer moniesRequested, 
			List<Good> sharesRequested, Integer moniesOffered, List<Good> sharesOffered) {
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
		if (fromAccount.monies < moniesOffered || !fromAccount.goods.containsAll(sharesOffered)) {
			return false;
		}
		
		if (toAccount.monies < moniesRequested || !toAccount.goods.containsAll(sharesRequested)) {
			return false;
		}
		
		return true;
	}

}
