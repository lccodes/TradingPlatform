package brown.generatepredictions;

import java.util.Map;

public interface IBidStrategy {
	
	public Map<Good, Price> getBids();
}
