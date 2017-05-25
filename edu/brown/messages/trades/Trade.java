package brown.messages.trades;

import brown.agent.Agent;
import brown.messages.Message;

/**
 * This message provides an avenue for agents to
 * either accept or decline a trade request. The initiating agent
 * can also cancel the TR with this method.
 */
public class Trade extends Message {
	public final NegotiateRequest tradeRequest;
	public final boolean accept;

	public Trade(int ID, NegotiateRequest tradeRequest, boolean accept) {
		super(ID);
		this.tradeRequest = tradeRequest;
		this.accept = accept;
	}

	@Override
	public void dispatch(Agent agent) {
		//Noop
	}

}
