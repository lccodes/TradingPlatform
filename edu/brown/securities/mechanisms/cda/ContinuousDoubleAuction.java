package brown.securities.mechanisms.cda;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.assets.value.SecurityType;
import brown.assets.value.ITradeable;
import brown.auctions.arules.MechanismType;
import brown.auctions.rules.ClearingRule;
import brown.auctions.twosided.TwoSidedAuction;
import brown.auctions.twosided.ITwoSidedWrapper;

public class ContinuousDoubleAuction implements TwoSidedAuction {
	private final Integer ID;
	private final FullType TYPE;
	private final ClearingRule RULE;
	
	/**
	 * For kryonet
	 * DO NOT USE
	 */
	public ContinuousDoubleAuction() {
		this.ID = null;
		this.TYPE = null;
		this.RULE = null;
	}
	
	/**
	 * Constructor
	 * @param ID : auction ID
	 * @param type : SecurityType
	 * @param rule : ClearingRule
	 */
	public ContinuousDoubleAuction(Integer ID, SecurityType type, ClearingRule rule) {
		this.ID = ID;
		this.TYPE = new FullType(type, null);
		this.RULE = rule;
	}
	
	/**
	 * Constructor
	 * @param ID : auction ID
	 * @param type : <SecurityType,Integer>
	 * @param rule : ClearingRule
	 */
	public ContinuousDoubleAuction(Integer ID, FullType type, ClearingRule rule) {
		this.ID = ID;
		this.TYPE = type;
		this.RULE = rule;
	}

	@Override
	public Integer getID() {
		return this.ID;
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
	public boolean isClosed() {
		return false;
	}

	@Override
	public MechanismType getMechanismType() {
		return MechanismType.ContinuousDoubleAuction;
	}

	@Override
	public SortedMap<Double, Set<Order>> getBuyBook() {
		return this.RULE.getBuyBook();
	}

	@Override
	public SortedMap<Double, Set<Order>> getSellBook() {
		return this.RULE.getSellBook();
	}

	@Override
	public ITwoSidedWrapper wrap() {
		return new CDAWrapper(this);
	}

	@Override
	public void tick(double time) {
		// Noop
	}

	@Override
	public boolean permitShort() {
		return this.RULE.isShort();
	}
}
