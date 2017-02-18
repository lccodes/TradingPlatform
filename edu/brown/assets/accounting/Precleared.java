package brown.assets.accounting;

import brown.assets.value.Tradeable;
import brown.securities.SecurityType;

public class Precleared implements Tradeable {
	private final Integer ID;
	private final double COUNT;
	private final double PRICE;
	private final SecurityType TYPE;
	
	public Precleared(Integer agentID, double count, double price, SecurityType type) {
		this.ID = agentID;
		this.COUNT = count;
		this.TYPE = type;
		this.PRICE = price;
	}

	@Override
	public Integer getAgentID() {
		return this.ID;
	}

	@Override
	public void setAgentID(Integer ID) {
		//Noop
	}

	@Override
	public double getCount() {
		return this.COUNT;
	}
	
	public SecurityType getType() {
		return this.TYPE;
	}
	
	public double getPrice() {
		return this.PRICE;
	}

}
