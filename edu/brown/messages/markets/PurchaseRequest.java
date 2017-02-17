package brown.messages.markets;

import brown.auctions.TwoSidedAuction;
import brown.messages.Message;
import brown.securities.Security;

public class PurchaseRequest extends Message {
	public final Security market;  //TODO: Delete
	
	public final Integer auctionID;
	public final double buyQuantity;
	public final double sellQuantity;
	public final double buyPrice;
	public final double sellPrice;
	
	public PurchaseRequest() {
		super(null);
		market = null;
		this.auctionID = null;
		buyQuantity = 0;
		sellQuantity = 0;
		
		this.buyPrice = 0;
		this.sellPrice = 0;
	}

	//TODO: Delete
	public PurchaseRequest(int ID, Security predictionMarket, double shareYes, double shareNo) {
		super(ID);
		this.market = predictionMarket;
		this.buyQuantity = shareYes;
		this.sellQuantity = shareNo;
		
		this.auctionID = null;
		this.buyPrice = 0;
		this.sellPrice = 0;
	}
	
	public PurchaseRequest(int ID, TwoSidedAuction auction, double buyQuantity, double buyPrice) {
		super(ID);
		this.auctionID = auction.getID();
		this.buyQuantity = buyQuantity;
		this.buyPrice = buyPrice;
		
		this.sellPrice = 0;
		this.sellQuantity = 0;
		
		this.market = null; //TODO: Delete
	}
	
	public PurchaseRequest(int ID, TwoSidedAuction auction, double buyQuantity) {
		super(ID);
		this.auctionID = auction.getID();
		this.buyQuantity = buyQuantity;
		this.buyPrice = Double.MAX_VALUE;
		
		this.sellPrice = 0;
		this.sellQuantity = 0;
		
		this.market = null; //TODO: Delete
	}
	
	public PurchaseRequest(TwoSidedAuction auction, double sellQuantity, double sellPrice, int ID) {
		super(ID);
		this.auctionID = auction.getID();
		this.buyQuantity = 0;
		this.buyPrice = 0;
		
		this.sellPrice = sellPrice;
		this.sellQuantity = sellQuantity;
		
		this.market = null; //TODO: Delete
	}
	
	public PurchaseRequest(TwoSidedAuction auction, double sellQuantity, int ID) {
		super(ID);
		this.auctionID = auction.getID();
		this.buyQuantity = 0;
		this.buyPrice = 0;
		
		this.sellPrice = Double.MIN_VALUE;
		this.sellQuantity = sellQuantity;
		
		this.market = null; //TODO: Delete
	}

}
