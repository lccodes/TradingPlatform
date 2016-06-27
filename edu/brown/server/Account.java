package brown.server;

import java.util.LinkedList;
import java.util.List;

public class Account {
	public final Integer ID;
	public final Integer monies;
	public final List<Share> shares;
	
	public Account(Integer ID) {
		this.ID = ID;
		this.monies = new Integer(0);
		this.shares = new LinkedList<Share>();
	}
	
	private Account(Integer ID, Integer monies, List<Share> shares) {
		this.ID = ID;
		this.monies = monies;
		this.shares = shares;
	}
	
	public Account add(Integer newMonies, List<Share> newShares) {
		if (newMonies == null || newShares == null) {
			return null;
		}
		
		this.shares.addAll(newShares);
		return new Account(this.ID, newMonies+this.monies, shares);
	}
	
	public Account remove(Integer removeMonies, List<Share> removeShares) {
		if (removeMonies == null || removeShares == null) {
			return null;
		}
		
		this.shares.removeAll(removeShares);
		return new Account(this.ID, this.monies-removeMonies, this.shares);
	}
}
