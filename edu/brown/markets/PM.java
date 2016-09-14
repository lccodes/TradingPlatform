package brown.markets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import brown.server.Account;
import brown.server.AgentServer;

/**
 * Private backend prediction market implementation
 * Agents are provided pointers to the public face
 * to prevent illegal modifications
 * @author lcamery
 *
 */
public abstract class PM {
	protected final List<Share> yes;
	protected final List<Share> no;
	protected final double b;
	protected boolean open;
	
	public PM(double b) {
		this.yes = new ArrayList<Share>();
		this.no = new ArrayList<Share>();
		this.b = b;
		this.open = true;
	}
	
	/*
	 * Cost function
	 * @param qd1 : new quantity yes
	 * @param qd2 : new quantity no
	 * @return cost : double
	 */
	private double cost(int newq1, int newq2) {
		if (!open) {
			return -1;
		}
		return b*Math.log(Math.pow(Math.E, (newq1 + yes.size())/b) + Math.pow(Math.E, (newq2+no.size())/b))
				- b*Math.log(Math.pow(Math.E, yes.size()/b) + Math.pow(Math.E, no.size()));
	}
	
	/*
	 * Quotes the cumulative price for a certain number of yes shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double getPriceYes(int shareNum) {
		return cost(shareNum, 0);
	}
	
	/*
	 * Quotes the cumulative price for a certain number of no shares
	 * @param shareNum : int
	 * @return cost : double
	 */
	public double getPriceNo(int shareNum) {
		return cost(0, shareNum);
	}
	
	/*
	 * Returns a share to an agent that buys yes
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 * @return share : share object; extendable in real games
	 */
	public abstract Share buyYes(Integer agentID, int shareNum);
	
	/*
	 * Returns a share to an agent that buys no
	 * @param agentID : agent's public ID
	 * @param shareNum : int
	 * @return share : share object; extendable in real games
	 */
	public abstract Share buyNo(Integer agentID, int shareNum);
	
	/*
	 * Closes the market and pays shareholders
	 */
	public boolean close(AgentServer server, boolean outcome) {
		List<Integer> IDs = new LinkedList<Integer>();
		for (Share share : yes) {
			Account account = server.publicToAccount(share.getAgentPublicId());
			List<Share> temp = new LinkedList<Share>();
			temp.add(share);
			account.remove(0, temp);
			account.add(1, new LinkedList<Share>());
			IDs.add(share.getAgentPublicId());
		}
		
		for (Share share : no) {
			Account account = server.publicToAccount(share.getAgentPublicId());
			List<Share> temp = new LinkedList<Share>();
			temp.add(share);
			account.remove(0, temp);
			IDs.add(share.getAgentPublicId());
		}
		
		server.sendBankUpdates(IDs);
		return true;
	}
	
}
