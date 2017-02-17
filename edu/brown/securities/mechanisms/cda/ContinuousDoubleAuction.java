package brown.securities.mechanisms.cda;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import brown.assets.accounting.SecurityTwo;
import brown.auctions.TwoSidedAuction;
import brown.auctions.TwoSidedWrapper;
import brown.auctions.arules.AllocationType;
import brown.securities.SecurityType;

public class ContinuousDoubleAuction implements TwoSidedAuction {
	private final Integer ID;
	private final SecurityType TYPE;
	private final SortedMap<Double, SecurityTwo> pendingBuy;
	private final SortedMap<Double, SecurityTwo> pendingSell;
	
	/**
	 * For kryonet
	 * DO NOT USE
	 */
	public ContinuousDoubleAuction() {
		this.ID = null;
		this.TYPE = null;
		this.pendingBuy = null;
		this.pendingSell = null;
	}
	
	/**
	 * Constructor
	 * @param ID : auction ID
	 * @param type : SecurityType
	 */
	public ContinuousDoubleAuction(Integer ID, SecurityType type) {
		this.ID = ID;
		this.TYPE = type;
		this.pendingBuy = new TreeMap<Double, SecurityTwo>(Collections.reverseOrder());
		this.pendingSell = new TreeMap<Double, SecurityTwo>();
	}

	@Override
	public Integer getID() {
		return this.ID;
	}

	@Override
	public SecurityType getType() {
		return this.TYPE;
	}

	@Override
	public List<SecurityTwo> buy(Integer agentID, double shareNum, double sharePrice) {
		List<SecurityTwo> completed = new LinkedList<SecurityTwo>();
		for (double amount : this.pendingSell.keySet()) {
			if (shareNum <= 0) {
				break;
			}
			
			SecurityTwo opp = this.pendingSell.get(amount);
			if (opp.getTransactedPrice() <= sharePrice) {
				shareNum += opp.getCount();
				completed.add(new SecurityTwo(this, opp.getCount(), 
						opp.getAgentID(), opp.getTransactedPrice(), true));
				completed.add(new SecurityTwo(this, Math.abs(opp.getCount()), 
						agentID, opp.getTransactedPrice(), true));
			} else {
				break;
			}
		}
		
		if (shareNum > 0) {
			SecurityTwo pending = new SecurityTwo(this, shareNum, agentID, sharePrice, false);
			this.pendingBuy.put(sharePrice, pending);
			completed.add(pending);
		}
		
		return completed;
	}

	@Override
	public List<SecurityTwo> sell(Integer agentID, double shareNum, double sharePrice) {
		List<SecurityTwo> completed = new LinkedList<SecurityTwo>();
		for (double amount : this.pendingBuy.keySet()) {
			if (shareNum <= 0) {
				break;
			}
			
			SecurityTwo opp = this.pendingBuy.get(amount);
			if (opp.getTransactedPrice() >= sharePrice) {
				shareNum -= opp.getCount();
				completed.add(new SecurityTwo(this, opp.getCount(), 
						opp.getAgentID(), opp.getTransactedPrice(), true));
				completed.add(new SecurityTwo(this, -1 * opp.getCount(), 
						agentID, opp.getTransactedPrice(), true));
			} else {
				break;
			}
		}
		
		if (shareNum > 0) {
			SecurityTwo pending = new SecurityTwo(this, -1 * shareNum, agentID, sharePrice, false);
			this.pendingBuy.put(sharePrice, pending);
			completed.add(pending);
		}
		
		return completed;
	}

	/**
	 * TODO: Include sharePrice
	 * @param buyQ
	 * @param sellQ
	 * @return
	 */
	public double cost(double buyQ, double sellQ) {
		double cost = 0;
		for (double amount : this.pendingSell.keySet()) {
			if (buyQ <= 0) {
				break;
			}
			
			cost += this.pendingSell.get(amount).getTransactedPrice() * buyQ;
			buyQ += this.pendingSell.get(amount).getCount();
		}
		
		for (double amount : this.pendingBuy.keySet()) {
			if (sellQ <= 0) {
				break;
			}
			
			cost += this.pendingBuy.get(amount).getTransactedPrice() * sellQ;
			buyQ -= this.pendingBuy.get(amount).getCount();
		}
		
		return cost == 0 ? Double.MAX_VALUE : cost;
	}

	@Override
	public double bid(double shareNum, double sharePrice) {
		return this.cost(shareNum, 0);
	}

	@Override
	public double ask(double shareNum, double sharePrice) {
		return this.cost(0, shareNum);
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public AllocationType getMechanismType() {
		return AllocationType.ContinuousDoubleAuction;
	}

	@Override
	public SortedMap<Double, SecurityTwo> getBuyBook() {
		return this.pendingBuy;
	}

	@Override
	public SortedMap<Double, SecurityTwo> getSellBook() {
		return this.pendingSell;
	}

	@Override
	public TwoSidedWrapper wrap() {
		return new CDAWrapper(this);
	}

}
