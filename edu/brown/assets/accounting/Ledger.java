package brown.assets.accounting;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import brown.securities.Security;
import brown.server.AgentServer;

public abstract class Ledger {
	protected List<Transaction> transactions;
	protected final Security security;
	
	public Ledger(Security security) {
		this.security = security;
		this.transactions = new LinkedList<Transaction>();
	}
	
	public void add(Transaction t) {
		this.transactions.add(t);
	}
	
	public Transaction get(int i) {
		return transactions.get(i);
	}
	
	public Iterator<Transaction> iterator() {
		return transactions.iterator();
	}
	
	public abstract void close(AgentServer server, boolean pay);

}
