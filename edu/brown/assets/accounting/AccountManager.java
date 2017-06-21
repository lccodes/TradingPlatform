package brown.assets.accounting; 

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AccountManager {
	private Map<Integer, Account> accounts;

	/**
	 * a manager that stores accounts for use in the server.
	 */
	public AccountManager() {
		this.accounts = new ConcurrentHashMap<Integer, Account>();
	}
	
	public void setAccount(Integer ID, Account account) {
		synchronized (ID) {
			accounts.put(ID, account);
		}
	}
	

	public Account getAccount(Integer ID) {
		return accounts.get(ID);
	}
	
	public List<Account> getAccounts() {
		List<Account> accountsList = new ArrayList<Account>(accounts.values());
		return accountsList;
	}
	
	public Boolean containsAcct(Integer ID) {
		return accounts.containsKey(ID);
	}

}