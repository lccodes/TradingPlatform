package brown.markets;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import brown.assets.accounting.Ledger;
import brown.assets.accounting.Order;
import brown.assets.value.FullType;
import brown.assets.value.TradeableType;
import brown.auctions.arules.MechanismType;
import brown.clearingrules.ClearingRule;
import brown.clearingrules.LMSRNoClearing;
import brown.clearingrules.LMSRYesClearing;
import brown.tradeables.Tradeable;

public class LMSRServer implements TwoSidedAuction {
	private final Integer ID;
	private final ClearingRule RULE;
	private final FullType TYPE;
	private final LMSRBackend BACKEND;
	
	public LMSRServer() {
		this.ID = null;
		this.RULE = null;
		this.TYPE = null;
		this.BACKEND = null;
	}
	
	public LMSRServer(Integer ID, boolean dir, LMSRBackend backend, boolean shortSelling) {
		this.ID = ID;
		this.RULE = dir ? new LMSRYesClearing(backend, shortSelling) : new LMSRNoClearing(backend, shortSelling);
		this.TYPE = new FullType(dir ? TradeableType.PredictionYes : TradeableType.PredictionNo, 
				backend.getId());
		this.BACKEND = backend;
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
	public ITwoSidedAuction wrap(Ledger ledger) {
		return new LMSR(this, ledger);
	}

	@Override
	public boolean permitShort() {
		return this.RULE.isShort();
	}

	@Override
	public void cancel(Integer agentID, boolean buy, double shareNum,
			double sharePrice) {
		//Noop		
	}

	/**
	 * Gets the market price
	 * @return price
	 */
	public double price() {
		return this.RULE.price();
	}
	
	/**
	 * How many shares does it take to fill this budget?
	 * @param monies
	 * @return shares
	 */
	public double moniesToShares(double monies) {
		return this.BACKEND.budgetToShares(monies, this.TYPE.TYPE == TradeableType.PredictionYes);
	}
	
	/**
	 * How many shares does it take to get to this price?
	 * @param price
	 * @return
	 */
	public double priceToShares(double price) {
		return this.BACKEND.howMany(price, this.TYPE.TYPE.equals(TradeableType.PredictionYes));
	}

}
