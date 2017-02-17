package brown.messages.auctions;

import java.util.Set;

import brown.assets.value.Tradeable;
import brown.auctions.bundles.BidBundle;
import brown.auctions.bundles.BundleType;
import brown.messages.Message;

/**
 * Message to bid on auctions.
 * 
 * @author lcamery
 */
public class TradeRequest extends Message {
  public final Integer AuctionID;
  public final BundleType BundleType;
  public final Set<Tradeable> Goods;
  public final BidBundle Current;

  /**
   * Kryo requires empty constructor DO NOT USE
   */
  public TradeRequest() {
    super(null);
    this.AuctionID = null;
    this.BundleType = null;
    this.Current = null;
    this.Goods = null;
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
  public TradeRequest(int ID, Integer auctionID, BundleType bundleType, BidBundle bundle, Set<Tradeable> goods) {
    super(ID);
    this.AuctionID = auctionID;
    this.BundleType = bundleType;
    this.Current = bundle;
    this.Goods = goods;
  }

}
