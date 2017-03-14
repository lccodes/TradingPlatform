package brown.messages.auctions;

import java.util.Set;

import brown.agent.Agent;
import brown.assets.value.ITradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.messages.Message;

/**
 * Message to bid on auctions.
 * 
 * @author lcamery
 */
public class BidRequest extends Message {
  public final Integer AuctionID;
  public final Set<ITradeable> Goods;
  public final BidBundle Current;
  public final BundleType TYPE;

  /**
   * Kryo requires empty constructor DO NOT USE
   */
  public BidRequest() {
    super(null);
    this.AuctionID = null;
    this.Current = null;
    this.Goods = null;
    this.TYPE = null;
  }

  /**
   * Constructor.
   *  
   * @param ID
   * @param auctionID
   * @param bundleType
   * @param bundle
   * @param goods
   */
  public BidRequest(int ID, Integer auctionID, BundleType type, BidBundle bundle, Set<ITradeable> goods) {
    super(ID);
    this.AuctionID = auctionID;
    this.Current = bundle;
    this.Goods = goods;
    this.TYPE = type;
  }

@Override
public void dispatch(Agent agent) {
	//Noop
}

}
