package brown.messages;

/**
 * Message provided to agents when their accounts change
 */
public class BankUpdate extends Message {
	public final Account oldAccount;
	public final Account newAccount;

	public BankUpdate(int ID, Account oldAccount, Account newAccount) {
		super(ID);
		this.oldAccount = oldAccount;
		this.newAccount = newAccount;
	}

}
