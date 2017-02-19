package brown.securities.prediction.simulator;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import brown.securities.mechanisms.lmsr.PMBackend;
import brown.securities.prediction.marketmakers.implementations.LiquiditySensitive;
import brown.securities.prediction.marketmakers.implementations.LukeMM;

public class Simulator {
	private final List<PMBackend> marketmakers;
	private final List<Bidder> agents;
	
	public Simulator(List<PMBackend> marketmakers, List<Bidder> agents) {
		this.marketmakers = marketmakers;
		this.agents = agents;
	}
	
	public SimulationResult simulate(boolean orderedAgents, boolean correct) {
		SimulationResult result = new SimulationResult();
		List<Bidder> theAgents = new LinkedList<Bidder>();
		theAgents.addAll(agents);
		if (!orderedAgents) {
			Collections.shuffle(theAgents);
		}
		
		for (Bidder agent : theAgents) {
			Comparator<PMBackend> mostGains = new Comparator<PMBackend>() {
				@Override
				public int compare(PMBackend mm1, PMBackend mm2) {
					double q1 = getQuantity(null, mm1, agent);
					double q2 = getQuantity(null, mm2, agent);
					if (q1 > q2) {
						return 1;
					} else if (q1 < q2) {
						return -1;
					}
					return 0;
				}
			};
			
			Collections.sort(marketmakers, mostGains);
			PMBackend bestmm = marketmakers.get(0);
			//System.out.println(bestmm.price(true) + " " + bestmm.price(false));
			double quantity = getQuantity(result, bestmm, agent);
			if (agent.value == -1) {
				if (bestmm.price(true) >= .5) {
					bestmm.yes(null, quantity);
				} else {
					bestmm.no(null, quantity);
				}
			} else {
				if (agent.value > bestmm.price(true)) {
					bestmm.yes(null, quantity);
				} else {
					bestmm.no(null, quantity);
				}
			}
			result.addPurchase(bestmm, agent, quantity, correct);
		}
		for (PMBackend mm : marketmakers) {
			result.addMarketmaker(mm);
		}
		
		return result;
	}
	
	private static double getQuantity(SimulationResult result, PMBackend mm, Bidder agent) {
		boolean dir = agent.value > mm.price(true);
		double value = agent.value;
		if (value == -1) {
			dir = mm.price(true) >= .5;
			value = dir ? 1 : 0;
		}
		double idealShareNum = mm.howMany(value, dir);
		double idealCost = dir ? mm.cost(idealShareNum, 0) : mm.cost(0, idealShareNum);
		double shareNum = idealCost > agent.budget ? mm.budgetToShares(agent.budget, dir) : idealShareNum;
		if (result != null) {
			//System.out.println(mm.alpha + " " + shareNum + " " + (dir ? mm.cost(shareNum, 0) : mm.cost(0, shareNum)));
			result.addCost(mm,dir ? mm.cost(shareNum, 0) : mm.cost(0, shareNum));
		}

		return shareNum;
	}
	
	public static double getWinner(double first, double second) {
		int f = 0, s = 0;
		for (int i = 0; i < 3; i++) {
			MarketMakerFactory mmf = new MarketMakerFactory();
			LiquiditySensitive uno = new LiquiditySensitive(first);
			LukeMM dos = new LukeMM(second);
			mmf.add(uno);
			mmf.add(dos);
			BidderFactory bf = new BidderFactory();
			int x = 100;
			while (x-- > 0) {
				bf.addBidder(Math.random(), 1);
			}
			Simulator simulator = new Simulator(mmf.make(), bf.getBidders());
			SimulationResult sr = simulator.simulate(false, Math.random() > .5);
			if (sr.getCost(uno) > sr.getCost(dos)) {
				f += 1;
			} else {
				s += 1;
			}
		}
		if (f > s) {
			return first;
		} else {
			return second;
		}
	}
	
	public static double getWinner(double first) {
		int f = 0, s = 0;
		for (int i = 0; i < 3; i++) {
			MarketMakerFactory mmf = new MarketMakerFactory();
			LiquiditySensitive uno = new LiquiditySensitive(first);
			PMBackend pmb = new PMBackend(50);
			mmf.add(uno);
			mmf.add(pmb);
			BidderFactory bf = new BidderFactory();
			int x = 100;
			while (x-- > 0) {
				bf.addBidder(Math.random(), 1);
			}
			Simulator simulator = new Simulator(mmf.make(), bf.getBidders());
			SimulationResult sr = simulator.simulate(false, Math.random() > .5);
			if (sr.getCost(uno) > sr.getCost(pmb)) {
				f += 1;
			} else {
				s += 1;
			}
		}
		if (f > s) {
			return first;
		} else {
			return -1;
		}
	}
	
	public static double getAccuracy(int which, int informed, int uninformed) {
			MarketMakerFactory mmf = new MarketMakerFactory();
			PMBackend pmb = null;
			switch(which) {
			case 0: pmb = new PMBackend(50);
			case 1: pmb = new LiquiditySensitive(.2);
			case 2: pmb = new LukeMM(.2);
			}
			mmf.add(pmb);
			BidderFactory bf = new BidderFactory();
			while (informed-- > 0) {
				bf.addBidder(Math.random(), 1);
			}
			double truth = bf.getAverage();
			while(uninformed-- > 0) {
				bf.addBidder(-1, 1);
			}
			Simulator simulator = new Simulator(mmf.make(), bf.getBidders());
			simulator.simulate(false, truth >= .5);
			//System.out.println(truth + " " + pmb.price(true));
			return pmb.price(true) - truth;
	}
	
	public static void main(String[] args) {
		/*double start = .9;
		double fin = .1;
		for (int i = 0; i < 10; i ++) {
			double win = Simulator.getWinner(fin, start);
			System.out.println(win + " " + (fin == win ? -1 : fin));
			fin += .1;
		}*/
		for (int i = 0; i < 3; i++) {
			double win = Simulator.getAccuracy(i, 20, 10);
			double win2 = Simulator.getAccuracy(i, 10, 20);
			System.out.println((Math.abs(win) < Math.abs(win2)) + " " + Math.abs(win - win2));
		}
		//System.out.println(sr);
		//System.out.println("Average: " + bf.getAverage());
		//for(PMBackend mm : mmf.make()) {
		//	System.out.println(mm.price(true));
		//}
	}

}
