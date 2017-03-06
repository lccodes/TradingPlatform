package brown.messages.trades;

import java.util.List;

import brown.assets.accounting.Account;
import brown.assets.value.ITradeable;

/**
 * A message sent to the server by an agent when it wants to initiate a trade note: -1 indicates offer to all agents
 * 
 * @author lcamery
 */
public class NegotiateRequest {
  public final Integer toID;
  public final Integer fromID;

  public final Integer moniesRequested;
  public final List<ITradeable> sharesRequested;

  public final Integer moniesOffered;
  public final List<ITradeable> sharesOffered;

  /**
   * Constructor.
   * 
   * @param toID
   * @param fromID
   * @param moniesRequested
   * @param sharesRequested
   * @param moniesOffered
   * @param sharesOffered
   */
  public NegotiateRequest(Integer toID, Integer fromID, Integer moniesRequested, List<ITradeable> sharesRequested, Integer moniesOffered, List<ITradeable> sharesOffered) {
    this.toID = toID;
    this.fromID = fromID;

    this.moniesRequested = moniesRequested;
    this.moniesOffered = moniesOffered;

    this.sharesOffered = sharesOffered;
    this.sharesRequested = sharesRequested;
  }

  /**
   * Overwrites the fromID field to prevent malicious offer creation
   * 
   * @param correctID
   * @return
   */
  public NegotiateRequest safeCopy(Integer correctID) {
    return new NegotiateRequest(toID, correctID, moniesRequested, sharesRequested, moniesOffered, sharesOffered);
  }

  /**
   * Method that determines if two agents' accounts satisfy the assets needed to execute this trade
   * 
   * @param toAccount
   * @param fromAccount
   * @return
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
