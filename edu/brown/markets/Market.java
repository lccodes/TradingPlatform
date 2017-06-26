package brown.markets;

import java.util.LinkedList;
import java.util.List;

import brown.allocationrules.AllocationRule;
import brown.assets.accounting.Ledger;
import brown.assets.accounting.Order;
import brown.bundles.BidBundle;
import brown.marketinternalstates.MarketInternalState;
import brown.messages.auctions.Bid;
import brown.messages.markets.TradeRequest;
import brown.paymentrules.PaymentRule;
import brown.rules.activityrules.ActivityRule;
import brown.rules.irpolicies.InformationRevelationPolicy;
import brown.rules.queryrules.QueryRule;
import brown.rules.terminationconditions.TerminationCondition;

public class Market {
	private final PaymentRule PRULE;
	private final AllocationRule ARULE;
	
	private final QueryRule QRULE;
	private final ActivityRule ACTRULE;
	private final InformationRevelationPolicy INFOPOLICY;
	
	private final TerminationCondition TCONDITION;
	
	private final MarketInternalState STATE;
	private int lastTerm = 0;
	private int term = 0;
	
	public Market(PaymentRule pRule, AllocationRule aRule, QueryRule qRule,
			InformationRevelationPolicy infoPolicy,  
			TerminationCondition tCondition, ActivityRule actRule, MarketInternalState startingState) {
		this.PRULE = pRule;
		this.ARULE = aRule;
		this.QRULE = qRule;
		this.ACTRULE = actRule;
		this.INFOPOLICY = infoPolicy;
		this.TCONDITION = tCondition;
		this.STATE = startingState;
		
		this.STATE.setReserve(this.PRULE.getReserve());
	}
	
	/**
	 * Pass through market ID
	 * @return
	 */
	public Integer getID() {
		return this.STATE.getID();
	}
	
	/**
	 * Constructs the trade request
	 * @param ID : agent who it's for
	 * @param ledger : past transactions for market
	 * @return TradeRequest for ID
	 */
	public TradeRequest wrap(Integer ID, Ledger ledger) {
		//System.out.println("STARTWRAP" + this.term);
		if (this.term != this.lastTerm) {
			//System.out.println("BIDS " + this.STATE.getBids());
			this.STATE.setAllocation(this.ARULE.getAllocation(this.STATE));
			this.STATE.setPayments(this.PRULE.getPayments(this.STATE));
			//System.out.println("ALLOC" + this.STATE.getAllocation());
			//System.out.println(this.STATE.getPayments());
			this.lastTerm = this.term;
		}

		return this.QRULE.wrap(ledger, this.PRULE.getPaymentType(),
				this.INFOPOLICY.handleInfo(ID, this.STATE));
	}
	
	/**
	 * Tests if the market has ended
	 * @return true if ended
	 */
	public boolean isOver() {
		return this.TCONDITION.isOver(this.STATE);
	}
	
	/**
	 * Whenever a new bid comes in
	 * @param bid
	 */
	public boolean handleBid(Bid bid) {
		if (this.ACTRULE.isAcceptable(this.STATE, bid)) {
			this.STATE.addBid(bid);
			return true;
		}
		return false;
	}
	
	/**
	 * Get the orders when the
	 * market ends
	 * @return List<Order> to process
	 */
	public List<Order> getOrders() {
		if (!this.isOver())  {
			return new LinkedList<Order>();
		}
		

		BidBundle newState = this.ARULE.getAllocation(this.STATE);
		this.STATE.setAllocation(newState);
		this.STATE.setPayments(this.PRULE.getPayments(this.STATE));
		return this.STATE.getPayments();
	}
	
	/**
	 * Ticks the market for clock auctions
	 * @param time
	 */
	public void tick(long time) {
		this.term++;
		this.STATE.tick(time);
	}
}
