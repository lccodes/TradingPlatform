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
public class BidReqeust extends Message {
  public final Integer AuctionID;
  public final Set<Tradeable> Goods;
  public final BidBundle Current;
  public final BundleType TYPE;

  /**
   * Kryo requires empty constructor DO NOT USE
   */
  public BidReqeust() {
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
  public BidReqeust(int ID, Integer auctionID, BundleType type, BidBundle bundle, Set<Tradeable> goods) {
    super(ID);
    this.AuctionID = auctionID;
    this.Current = bundle;
    this.Goods = goods;
    this.TYPE = type;
  }

}