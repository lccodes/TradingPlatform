package brown.securities.prediction.simulator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BidderFactory {
	private final List<Bidder> bidders;
	
	public BidderFactory() {
		this.bidders = new LinkedList<Bidder>();
	}
	
	public void addBidder(double value, double budget) {
		this.bidders.add(new Bidder(value, budget));
	}
	
	public List<Bidder> getBidders() {
		return this.bidders;
	}
	
	public double getAverage() {
		double avg = 0;
		double budgets = 0;
		for (Bidder bidder : bidders) {
			avg += bidder.value * bidder.budget;
			budgets += bidder.budget;
		}
		
		return avg == 0 ? .5 : avg/budgets;
	}
	
	public void shuffle() {
		Collections.shuffle(this.bidders);
	}

}
