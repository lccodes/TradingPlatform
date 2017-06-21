package brown.server.library;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Ledger;
import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.auctions.arules.MechanismType;
import brown.clearingrules.ClearingRule;
import brown.markets.ContinuousDoubleAuction;
import brown.markets.ITwoSidedAuction;
import brown.markets.TwoSidedAuction;
import brown.tradeables.Tradeable;

public class CDAServer implements TwoSidedAuction {
	private final Integer ID;
	private final FullType TYPE;
	private final ClearingRule RULE;
	
	/**
	 * For kryonet
	 * DO NOT USE
	 */
	public CDAServer() {
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
	public CDAServer(Integer ID, FullType type, ClearingRule rule) {
		this.ID = ID;
		this.TYPE = type;
		this.RULE = rule;
	}

	@Override
	public Integer getID() {
		return this.ID;
	}

	@Override
	public FullType getTradeableType() {
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
	public ITwoSidedAuction wrap(Ledger ledger) {
		return new ContinuousDoubleAuction(this, ledger);
	}

	@Override
	public void tick(double time) {
		// Noop
	}

	@Override
	public boolean permitShort() {
		return this.RULE.isShort();
	}

	@Override
	public void cancel(Integer agentID, boolean buy, double shareNum,
			double sharePrice) {
		this.RULE.cancel(agentID, buy, shareNum, sharePrice);		
	}
}
