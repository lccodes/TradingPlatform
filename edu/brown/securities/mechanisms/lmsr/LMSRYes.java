package brown.securities.mechanisms.lmsr;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Account;
import brown.assets.accounting.Security;
import brown.assets.accounting.Transaction;
import brown.assets.value.SecurityType;
import brown.assets.value.Tradeable;
import brown.auctions.TwoSidedAuction;
import brown.auctions.TwoSidedWrapper;
import brown.auctions.arules.AllocationType;

public class LMSRYes implements TwoSidedAuction {
	private final Integer MARKETID;
	private final PMBackend BACKEND;
	private final Map.Entry<SecurityType, Integer> TYPE;
	
	public LMSRYes() {
		this.MARKETID = null;
		this.BACKEND = null;
		this.TYPE = null;
	}
	
	public LMSRYes(Integer marketID, PMBackend backend, Integer securityID) {
		this.MARKETID = marketID;
		this.BACKEND = backend;
		this.TYPE = new AbstractMap.SimpleEntry<SecurityType, Integer>(SecurityType.PredictionYes, securityID);
	}

	@Override
	public Integer getID() {
		return this.MARKETID;
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public AllocationType getMechanismType() {
		return AllocationType.LMSR;
	}

	@Override
	public Map.Entry<SecurityType, Integer> getType() {
		return this.TYPE;
	}

	@Override
	public List<Transaction> bid(Integer agentID, double shareNum, double sharePrice) {
		double cost = this.BACKEND.bid(shareNum);
		this.BACKEND.yes(null, shareNum);
		List<Transaction> trans = new LinkedList<Transaction>();
		Security newSec = new Security(agentID, shareNum, this.TYPE,
				state -> state.getState() == 1 ? new Account(null).add(1) : null);
		trans.add(new Transaction(agentID, null, cost, shareNum, newSec));
		return trans;
	}

	@Override
	public List<Transaction> ask(Integer agentID, Tradeable opp, double sharePrice) {
		double cost = this.BACKEND.bid(-1 * opp.getCount());
		this.BACKEND.yes(null, -1 *opp.getCount());
		List<Transaction> trans = new LinkedList<Transaction>();
		trans.add(new Transaction(null, agentID, cost, opp.getCount(), opp));
		return trans;
	}

	@Override
	public double quoteBid(double shareNum, double sharePrice) {
		return this.BACKEND.bid(shareNum);
	}

	@Override
	public double quoteAsk(double shareNum, double sharePrice) {
		return this.BACKEND.ask(shareNum);
	}

	@Override
	public SortedMap<Double, Set<Transaction>> getBuyBook() {
		// Noop
		return null;
	}

	@Override
	public SortedMap<Double, Set<Tradeable>> getSellBook() {
		// Noop
		return null;
	}

	@Override
	public void tick(double time) {
		//Noop
	}

	@Override
	public TwoSidedWrapper wrap() {
		// TODO Auto-generated method stub
		return null;
	}

}
