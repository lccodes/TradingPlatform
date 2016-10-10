package brown.assets.accounting;

import java.util.LinkedList;
import java.util.List;

public class Account {
	public final Integer ID;
	public final double monies;
	public final List<Transaction> transactions;
	
	/**
	 * Kryo objects require a blank constructor
	 */
	public Account() {
		this.ID = null;
		this.monies = 0;
		this.transactions = null;
	}
	
	public Account(Integer ID) {
		this.ID = ID;
		this.monies = new Integer(0);
		this.transactions = new LinkedList<Transaction>();
	}
	
	private Account(Integer ID, double monies, List<Transaction> Transactions) {
		this.ID = ID;
		this.monies = monies;
		this.transactions = Transactions;
	}
	
	public Account add(double newMonies, List<Transaction> newTransactions) {
		if (newTransactions == null) {
			return new Account(this.ID, newMonies+this.monies, transactions);
		}
		
		this.transactions.addAll(newTransactions);
		return new Account(this.ID, newMonies+this.monies, transactions);
	}
	
	public Account remove(double removeMonies, List<Transaction> removeTransactions) {
		if (removeTransactions == null) {
			return new Account(this.ID, this.monies-removeMonies, this.transactions);
		}
		
		this.transactions.removeAll(removeTransactions);
		return new Account(this.ID, this.monies-removeMonies, this.transactions);
	}
}
