package brown.messages;

public class Trade extends Message {
	public final TradeRequest tradeRequest;
	public final boolean accept;

	public Trade(int ID, TradeRequest tradeRequest, boolean accept) {
		super(ID);
		this.tradeRequest = tradeRequest;
		this.accept = accept;
	}

}
