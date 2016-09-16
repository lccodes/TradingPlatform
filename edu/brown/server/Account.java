package brown.server;

import java.util.LinkedList;
import java.util.List;

import brown.markets.Share;

public class Account {
	public final Integer ID;
	public final double monies;
	public final List<Share> shares;
	
	public Account(Integer ID) {
		this.ID = ID;
		this.monies = new Integer(0);
		this.shares = new LinkedList<Share>();
	}
	
	private Account(Integer ID, double monies, List<Share> shares) {
		this.ID = ID;
		this.monies = monies;
		this.shares = shares;
	}
	
	public Account add(double newMonies, List<Share> newShares) {
		if (newShares == null) {
			return new Account(this.ID, newMonies+this.monies, shares);
		}
		
		this.shares.addAll(newShares);
		return new Account(this.ID, newMonies+this.monies, shares);
	}
	
	public Account remove(double removeMonies, List<Share> removeShares) {
		if (removeShares == null) {
			return new Account(this.ID, this.monies-removeMonies, this.shares);
		}
		
		this.shares.removeAll(removeShares);
		return new Account(this.ID, this.monies-removeMonies, this.shares);
	}
}
