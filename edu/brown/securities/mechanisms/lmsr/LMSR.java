package brown.securities.mechanisms.lmsr;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.assets.value.TradeableType;
import brown.assets.value.ITradeable;
import brown.auctions.arules.MechanismType;
import brown.auctions.crules.LMSRNoClearing;
import brown.auctions.crules.LMSRYesClearing;
import brown.auctions.rules.ClearingRule;
import brown.auctions.twosided.TwoSidedAuction;
import brown.auctions.twosided.ITwoSidedWrapper;

public class LMSR implements TwoSidedAuction {
	private final Integer ID;
	private final ClearingRule RULE;
	private final FullType TYPE;
	
	public LMSR() {
		this.ID = null;
		this.RULE = null;
		this.TYPE = null;
	}
	
	public LMSR(Integer ID, boolean dir, LMSRBackend backend, boolean shortSelling) {
		this.ID = ID;
		this.RULE = dir ? new LMSRYesClearing(backend, shortSelling) : new LMSRNoClearing(backend, shortSelling);
		this.TYPE = new FullType(dir ? TradeableType.PredictionYes : TradeableType.PredictionNo, 
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
	public List<Order> sell(Integer agentID, ITradeable opp, double sharePrice) {
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
	public ITwoSidedWrapper wrap() {
		return new LMSRWrapper(this);
	}

	@Override
	public boolean permitShort() {
		return this.RULE.isShort();
	}

}
