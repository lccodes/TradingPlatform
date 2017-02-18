package brown.assets.value;

import java.util.Set;

import brown.assets.accounting.Account;
import brown.securities.SecurityType;

public interface TradeableManager {

	public SecurityType getType();

	public Tradeable get(Integer to, Integer from, double cost, Set<Tradeable> goods);

	public Account close(Tradeable t);

}
