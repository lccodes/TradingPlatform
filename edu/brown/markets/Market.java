package brown.markets;

import brown.assets.Share;
import brown.server.AgentServer;

public interface Market {
	
	public Share buyPositive(Integer agentID, int shareNum);
	public Share buyNegative(Integer agentID, int shareNum);
	
	public void sellPositive(Integer agentID, int shareNum);
	public void sellNegative(Integer agentID, int shareNum);
	
	public double cost(int newq1, int newq2);
	public double pricePositive(int shareNum);
	public double priceNegative(int shareNum);
	
	public boolean close(AgentServer server, boolean outcome);
	public MarketWrapper wrap();
}
