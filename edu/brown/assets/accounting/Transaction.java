package brown.assets.accounting;

import java.util.Date;

import brown.securities.Security;

public interface Transaction {
	public Security getSecurity();
	
	public int getCount();
	
	public Integer getAgentID();
	
	public double getTransactedPrice();
	
	public Date getTimestamp();
}
