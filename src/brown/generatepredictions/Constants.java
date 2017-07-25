package brown.generatepredictions;

import java.util.HashSet;
import java.util.Set;

public abstract class Constants {
	static final int NUM_GOODS = 8; 
	static final int NUM_PRICES = 100;
	static final int NUM_ITERATIONS=100;
	static final double MAX_VAL=1.0;
	static final double MIN_VAL=0.0;
	static final int NUM_AGENTS=8;
	static final Set<Good> goodSet= new HashSet<Good>();
	static final Set<Agent> agentSet = new HashSet<Agent>();
}
