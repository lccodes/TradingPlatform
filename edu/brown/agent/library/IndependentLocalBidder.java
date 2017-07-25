package brown.agent.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import brown.assets.value.BasicType;
import brown.exceptions.AgentCreationException;
import brown.markets.SimpleAuction;
import brown.messages.markets.GameReport;
import brown.setup.Logging;

public class IndependentLocalBidder extends Lab8Agent {
	private Integer ROUNDS = 100;  

	public IndependentLocalBidder(String host, int port) throws AgentCreationException {
		super(host, port);
	}
	
	private Map<BasicType, Double> localBidStrategy (Map<Set<BasicType>, Double> bundles,
			Map<BasicType, Double> predictions, Integer rounds) {
		Map<BasicType, Double> toBid = predictions;
		for(int i = 0; i < rounds; i++) {
			for (Entry<BasicType, Double> good : toBid.entrySet()) { 
				//localbid produces the 
				
				//the vector P = the expected price distribution under the predictions. 
				//so although this is a stochastic problem, it's really solveable linearly
				//given a set of expectations. 
				
				//i.e. expected MU will bid expected marginal utility
				
			}
		}
		return toBid;
	}

	@Override
	public void onSimpleSealed(SimpleAuction market) {
		//change all of this to implement the localbid strategy
		System.out.println("Start");
		Map<BasicType, Double> predictions = new HashMap<BasicType,Double>();
		
		for (Entry<Set<BasicType>, Double> types : this.myValuation.entrySet()) {
			for (BasicType type : types.getKey()) {
				predictions.put(type, types.getValue()/(double)types.getKey().size());
			}
			System.out.println("WORKING");
		}
		Map<BasicType, Double> toBid = localBidStrategy(this.myValuation, predictions, ROUNDS);
		System.out.println("DONE");
		market.bid(this, toBid);
	}
	
	@Override
	public void onSimpleOpenOutcry(SimpleAuction market) {
		Set<BasicType> toBid = new HashSet<BasicType>();
		for (Set<BasicType> types : this.myValuation.keySet()) {
			for (BasicType type : types) {
				if (market.getMarketState(type).PRICE < Math.min(100,this.myValuation.get(types))) {
					toBid.add(type);
				}
			}
		}
		
		if (toBid.size() != 0) {
			market.demandSet(this, toBid);
		}
	}

	@Override
	public void onMarketUpdate(GameReport marketUpdate) {
		Logging.log("Market outcome:");
		//TODO: Log market outcome
	}
	
	public static void main(String[] args) throws AgentCreationException {
		new Lab8Demo("localhost", 2121);
		while(true){}
	}

}