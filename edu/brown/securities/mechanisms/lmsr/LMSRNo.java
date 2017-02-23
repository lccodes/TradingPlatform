package brown.securities.mechanisms.lmsr;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Account;
import brown.assets.accounting.Security;
import brown.assets.accounting.Transaction;
import brown.assets.value.FullType;
import brown.assets.value.SecurityType;
import brown.assets.value.Tradeable;
import brown.auctions.TwoSidedAuction;
import brown.auctions.TwoSidedWrapper;
import brown.auctions.arules.AllocationType;

public class LMSRNo implements TwoSidedAuction {
	private final Integer ID;
	private final LMSRBackend BACKEND;
	private final FullType TYPE;
	
	public LMSRNo() {
		this.ID = null;
		this.BACKEND = null;
		this.TYPE = null;
	}
	
	public LMSRNo(Integer ID, LMSRBackend backend) {
		this.ID = ID;
		this.BACKEND = backend;
		this.TYPE = new FullType(SecurityType.PredicitonNo, backend.getId());
	}

	@Override
	public Integer getID() {
		return this.ID;
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
	public FullType getType() {
		return this.TYPE;
	}

	@Override
	public List<Transaction> bid(Integer agentID, double shareNum, double sharePrice) {
		double cost = this.BACKEND.ask(shareNum);
		this.BACKEND.no(null, shareNum);
		List<Transaction> trans = new LinkedList<Transaction>();
		Security newSec = new Security(agentID, shareNum, this.TYPE,
				state -> state.getState() == 0 ? new Account(null).add(1) : null);
		trans.add(new Transaction(agentID, null, cost, shareNum, newSec));
		return trans;
	}

	@Override
	public List<Transaction> ask(Integer agentID, Tradeable opp, double sharePrice) {
		double cost = this.BACKEND.ask(-1 * opp.getCount());
		this.BACKEND.no(null, -1 *opp.getCount());
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
		// Noop
	}

	@Override
	public TwoSidedWrapper wrap() {
		return new LMSRWrapper(this);
	}

}
