package brown.assets.accounting; 

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Account manager stores accounts for use in the server.
 * @author acoggins
 *
 */
public class AccountManager {
	private Map<Integer, Account> accounts;

	/**
	 * manager that stores accounts for use in the server.
	 */
	public AccountManager() {
		this.accounts = new ConcurrentHashMap<Integer, Account>();
	}
	
	/**
	 * put an account in the account manager.
	 * @param ID
	 * ID of agent whose account is put in the manager.
	 * @param account
	 * account of agent
	 */
	public void setAccount(Integer ID, Account account) {
		synchronized (ID) {
			accounts.put(ID, account);
		}
	}
	
	/**
	 * gets account from an Agent's private id
	 * @param ID
	 * agent's private id
	 * @return
	 * an account
	 */
	public Account getAccount(Integer ID) {
		return accounts.get(ID);
	}
	
	/**
	 * get all accounts in the manager, in a List.
	 * @return
	 */
	public List<Account> getAccounts() {
		List<Account> accountsList = new ArrayList<Account>(accounts.values());
		return accountsList;
	}
	
	/**
	 * does the manager contain an account associated with the input ID? 
	 * @param ID
	 * agent's private ID
	 * @return
	 * whether or not the account exists. 
	 */
	public Boolean containsAcct(Integer ID) {
		return accounts.containsKey(ID);
	}

}