package brown.auctions.termination;

import java.util.LinkedList;
import java.util.List;

import brown.assets.accounting.Order;
import brown.auctions.interfaces.MarketInternalState;
import brown.auctions.interfaces.TerminationCondition;

public class SamePaymentsTermination implements TerminationCondition {
	private List<Order> lastPayments;
	private int sameness;
	private final int original;
	private boolean done;
	
	public SamePaymentsTermination() {
		this.lastPayments = new LinkedList<Order>();
		this.sameness = 2;
		this.original = 2;
		this.done = false;
	}

	public SamePaymentsTermination(int i) {
		this.sameness = i;
		this.original = i;
		this.lastPayments = new LinkedList<Order>();
		this.done = false;
	}

	@Override
	public boolean isOver(MarketInternalState state) {
		if (done) {
			return done;
		}
		boolean toReturn = true;
		//System.out.println("COMPARE " + this.sameness);
		//System.out.println(this.lastPayments);
		//System.out.println(state.getPayments());
		//System.out.println("----");
		if (state.getPayments() == null || this.lastPayments.size() != state.getPayments().size()) {
			toReturn = false;
			this.sameness = this.original;
		} else {
			for (int i = 0; i < this.lastPayments.size(); i++) {
				Order o1 = this.lastPayments.get(i);
				Order o2 = state.getPayments().get(i);
				if (!o1.TO.equals(o2.TO) || o1.COST != o2.COST) {
					toReturn = false;
					this.sameness = this.original;
					break;
				}
			}
		}
		
		this.lastPayments = state.getPayments() != null ? state.getPayments() : this.lastPayments;
		if (toReturn && sameness-- <= 0) {
			this.done = true;
		} else if (!toReturn) {
			this.sameness = this.original;
		}
		return done;
	}

}
