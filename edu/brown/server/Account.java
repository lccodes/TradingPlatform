package brown.server;

import java.util.LinkedList;
import java.util.List;

public class Account {
	public Integer ID;
	public Integer monies;
	public List<Share> shares;
	
	public Account(Integer ID) {
		this.ID = ID;
		this.monies = new Integer(0);
		this.shares = new LinkedList<Share>();
	}
}
