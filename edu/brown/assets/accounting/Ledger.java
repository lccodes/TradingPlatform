package brown.assets.accounting;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import brown.securities.Security;
import brown.server.AgentServer;

public abstract class Ledger {
	protected List<Transaction> transactions;
	protected final Security security;
	
	/**
	 * Constructs a ledger for the given security
	 * @param security : security that all transactions will refer to
	 */
	public Ledger(Security security) {
		this.security = security;
		this.transactions = new LinkedList<Transaction>();
	}
	
	/**
	 * Add a transaction
	 * @param t : transaction to add
	 */
	public void add(Transaction t) {
		this.transactions.add(t);
	}
	
	/**
	 * Gets a transaction
	 * @param i : the index of the transaction to get
	 * @return transaction i
	 */
	public Transaction get(int i) {
		return transactions.get(i);
	}
	
	/**
	 * Constructs an iterator over the transactions
	 * @return iterator
	 */
	public Iterator<Transaction> iterator() {
		return transactions.iterator();
	}
	
	/**
	 * Specifies the closure process and resolution for every
	 * transaction depending on how the security operates
	 * @param server : the agentServer
	 * @param pay : whether or not to pay out the security
	 */
	public abstract void close(AgentServer server, boolean pay);
	
	/**
	 * Pays out every agent with a transaction and returns their
	 * IDs for updating
	 * @param server : AgentServer for the information on agents
	 * @return set of IDs for paid agents
	 */
	protected Set<Integer> pay(AgentServer server) {
		Set<Integer> ids = new HashSet<Integer>();
		for (Transaction t : this.transactions) {
			Account account = server.publicToAccount(t.getAgentID());
			synchronized(account) {
				Account newAccount = account.addAll(t.getCount(), null);
				server.setAccount(t.getAgentID(), newAccount);
			}
			ids.add(account.ID);
		}
		
		return ids;
	}

}
