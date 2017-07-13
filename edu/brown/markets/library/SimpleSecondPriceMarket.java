package brown.markets.library;

import java.util.Set;

import brown.marketinternalstates.SimpleInternalState;
import brown.markets.Market;
import brown.rules.activityrules.OneShotActivity;
import brown.rules.allocationrules.SimpleHighestBidderAllocation;
import brown.rules.irpolicies.library.AnonymousPolicy;
import brown.rules.paymentrules.library.SimpleSecondPrice;
import brown.rules.queryrules.library.OutcryQueryRule;
import brown.rules.terminationconditions.OneShotTermination;
import brown.tradeables.Tradeable;

/**
 * Market preset for a single good, second price auction.
 * use this in the implementation of a game server to produce 
 * this auction.
 * @author acoggins
 *
 */
public class SimpleSecondPriceMarket {
  
  private Market market;
  
  /**
   * Constructor for this market. 
   * @param marketId
   * the id for the market to be opened. 
   * @param goodSet
   * the set of goods (here a singleton) that the market is opened with.
   */
  public SimpleSecondPriceMarket(int marketId, Set<Tradeable> goodSet) {
    this.market = new Market(new SimpleSecondPrice(), new SimpleHighestBidderAllocation(),
        new OutcryQueryRule(), new AnonymousPolicy(), new OneShotTermination(), 
        new OneShotActivity(), new SimpleInternalState(marketId, goodSet));
  }
  
  public Market getMarket() {
    return market; 
  }
}