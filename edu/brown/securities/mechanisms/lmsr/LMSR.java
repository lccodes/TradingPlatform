package brown.securities.mechanisms.lmsr;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.assets.value.SecurityType;
import brown.assets.value.Tradeable;
import brown.auctions.arules.MechanismType;
import brown.auctions.crules.LMSRNoClearing;
import brown.auctions.crules.LMSRYesClearing;
import brown.auctions.rules.ClearingRule;
import brown.auctions.twosided.TwoSidedAuction;
import brown.auctions.twosided.TwoSidedWrapper;

public class LMSR implements TwoSidedAuction {
	private final Integer ID;
	private final ClearingRule RULE;
	private final FullType TYPE;
	
	public LMSR() {
		this.ID = null;
		this.RULE = null;
		this.TYPE = null;
	}
	
	public LMSR(Integer ID, boolean dir, LMSRBackend backend) {
		this.ID = ID;
		this.RULE = dir ? new LMSRYesClearing(backend) : new LMSRNoClearing(backend);
		this.TYPE = new FullType(dir ? SecurityType.PredictionYes : SecurityType.PredictionNo, 
				backend.getId());
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
	public MechanismType getMechanismType() {
		return MechanismType.LMSR;
	}

	@Override
	public FullType getType() {
		return this.TYPE;
	}

	@Override
	public List<Order> buy(Integer agentID, double shareNum, double sharePrice) {
		return this.RULE.buy(agentID, shareNum, sharePrice);
	}

	@Override
	public List<Order> sell(Integer agentID, Tradeable opp, double sharePrice) {
		return this.RULE.sell(agentID, opp, sharePrice);
	}

	@Override
	public double quoteBid(double shareNum, double sharePrice) {
		return this.RULE.quoteBid(shareNum, sharePrice);
	}

	@Override
	public double quoteAsk(double shareNum, double sharePrice) {
		return this.RULE.quoteAsk(shareNum, sharePrice);
	}

	@Override
	public SortedMap<Double, Set<Order>> getBuyBook() {
		// Noop
		return null;
	}

	@Override
	public SortedMap<Double, Set<Order>> getSellBook() {
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
