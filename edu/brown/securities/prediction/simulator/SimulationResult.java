package brown.securities.prediction.simulator;

import java.util.HashMap;
import java.util.Map;

import brown.securities.mechanisms.lmsr.LMSRBackend;

public class SimulationResult {
	private Map<LMSRBackend, Integer> marketmakerID;
	private Map<Bidder, Integer> agentID;
	
	public final Map<Integer, Double> prices;
	public final Map<Integer, Double> values;
	public final Map<Integer, Double> quantities;
	public final Map<Integer, Integer> selections;
	public final Map<Integer, Double> costs;
	public final Map<Integer, Double> markup;
	
	private int agentCount = 0;
	private int mmCount = 0;
	
	public SimulationResult() {
		this.marketmakerID = new HashMap<LMSRBackend, Integer>();
		this.agentID = new HashMap<Bidder, Integer>();
		this.prices = new HashMap<Integer, Double>();
		this.values = new HashMap<Integer, Double>();
		this.quantities = new HashMap<Integer, Double>();
		this.selections = new HashMap<Integer, Integer>();
		this.costs = new HashMap<Integer, Double>();
		this.markup = new HashMap<Integer, Double>();
	}

	public void addPurchase(LMSRBackend mm, Bidder agent, double quantity, boolean correct) {
		selections.put(getID(agent), getID(mm));
		quantities.put(getID(agent), quantity);
		//System.out.println(quantity);
		//double penalty = correct == (agent.value > .5) ? -1*quantity : 0;
		//costs.put(getID(mm), costs.getOrDefault(getID(mm), 0.0) + penalty);
	}

	public void addMarketmaker(LMSRBackend mm) {
		prices.put(getID(mm), mm.price(true));
	}
	
	public void addCost(LMSRBackend mm, double cost) {
		costs.put(getID(mm), costs.getOrDefault(getID(mm), 0.0) + cost);
	}
	
	public double getCost(LMSRBackend mm) {
		return costs.get(getID(mm));
	}
	
	public int getID(LMSRBackend mm) {
		Integer id = marketmakerID.get(mm);
		if (id == null) {
			id = new Integer(mmCount++);
			marketmakerID.put(mm, id);
			markup.put(id, mm.alpha);
		}
		return id;
	}
	
	public int getID(Bidder agent) {
		Integer id = agentID.get(agent);
		if (id == null) {
			id = new Integer(agentCount++);
			agentID.put(agent, id);
			values.put(id, ((int)(100 * agent.value)/100.0));
		}
		return id;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Income (MM,a,$):\n");
		for (Map.Entry<Integer, Double> cost : costs.entrySet()) {
			builder.append(cost.getKey() + "," + markup.get(cost.getKey()) + "," + cost.getValue() + "\n");
		}
		builder.append("\nSelections (A,v,MM):\n");
		float share = 0;
		for (Map.Entry<Integer, Integer> select : selections.entrySet()) {
			builder.append(select.getKey() +","+ values.get(select.getKey()) + "," + select.getValue() + "\n");
			share += select.getValue();
		}
		
		builder.append("\nShare (MM,%)\n");
		builder.append("0," + (1.0-(share/selections.size())));
		builder.append("\n1," + (share/selections.size()));
		return builder.toString() + "\n";
	}
}
