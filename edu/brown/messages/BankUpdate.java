package brown.messages;

import brown.assets.accounting.Account;

/**
 * Message provided to agents when their accounts change
 */
public class BankUpdate extends Message {
	public final Account oldAccount;
	public final Account newAccount;
	
	/**
	 * Empty constructor for kryo
	 * DO NOT USE
	 */
	public BankUpdate() {
		super(null);
		this.oldAccount = null;
		this.newAccount = null;
	}

	/**
	 * Constructor for BankUpdate
	 * @param ID : agent ID
	 * @param oldAccount : old account
	 * @param newAccount : new account; contains info relevant to update
	 */
	public BankUpdate(int ID, Account oldAccount, Account newAccount) {
		super(ID);
		this.oldAccount = oldAccount;
		this.newAccount = newAccount;
	}

}
