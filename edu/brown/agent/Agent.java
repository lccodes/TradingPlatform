package brown.agent;

import java.io.IOException;

import brown.auctions.onesided.SimpleOneSidedWrapper;
import brown.exceptions.AgentCreationException;
import brown.messages.Ack;
import brown.messages.BankUpdate;
import brown.messages.Message;
import brown.messages.Registration;
import brown.messages.auctions.BidReqeust;
import brown.messages.markets.GameReport;
import brown.messages.trades.NegotiateRequest;
import brown.securities.mechanisms.cda.CDAWrapper;
import brown.securities.mechanisms.lmsr.LMSRWrapper;
import brown.setup.Setup;
import brown.setup.Startup;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

/**
 * All bidding agents will implement this class It abstracts away the
 * communication issues and let's authors focus on writing bidding logic.
 * 
 * @author lcamery
 */
public abstract class Agent {
	/**
	 * Kryo's client object.
	 */
	public final Client CLIENT;

	/**
	 * Agent id.
	 */
	public Integer ID;

	/**
	 * Implementations should always invoke super()
	 * 
	 * @param host
	 * @param port
	 * @param gameSetup
	 * @throws AgentCreationException
	 */
	public Agent(String host, int port, Setup gameSetup)
			throws AgentCreationException {
		this.CLIENT = new Client();
		this.ID = null;

		CLIENT.start();
		Log.TRACE();
		Kryo agentKryo = CLIENT.getKryo();
		Startup.start(agentKryo);
		if (gameSetup != null) {
			gameSetup.setup(agentKryo);
		}

		try {
			CLIENT.connect(5000, host, port, port);
		} catch (IOException e) {
			throw new AgentCreationException("Failed to connect to server");
		}

		final Agent agent = this;
		CLIENT.addListener(new Listener() {
			public void received(Connection connection, Object message) {
				synchronized (agent) {
					if (message instanceof Message) {
						Message theMessage = (Message) message;
						theMessage.dispatch(agent);
					}
				}
			}
		});

		CLIENT.sendTCP(new Registration(-1));
	}

	/**
	 * Provides response to sealed bid auction
	 * @param SealedBid wrapper
	 */
	public abstract void onSimpleSealed(SimpleOneSidedWrapper market);

	/**
	 * Provides agent response to OpenOutcry auction
	 * @param OpenOutcry wrapper
	 */
	public abstract void onSimpleOpenOutcry(SimpleOneSidedWrapper market);

	/**
	 * Provides agent response to LMSR
	 * @param LMSR wrapper
	 */
	public abstract void onLMSR(LMSRWrapper market);

	/**
	 * Provides agent response to CDAs
	 * @param market : CDA wrapper
	 */
	public abstract void onContinuousDoubleAuction(CDAWrapper market);

	/**
	 * Agents must accept their IDs from the server
	 * 
	 * @param registration
	 *            : includes the agent's new ID
	 */
	public void onRegistration(Registration registration) {
		this.ID = registration.getID();
	}

	/**
	 * Whenever a request is accepted/rejected, this method is sent with the
	 * request
	 * 
	 * @param rejection
	 *            : includes the rejected method and might say why
	 */
	public abstract void onAck(Ack message);

	/**
	 * Whenever you get a report
	 * @param marketUpdate
	 */
	public abstract void onMarketUpdate(GameReport marketUpdate);

	/**
	 * Whenever an agent's bank changes, the server sends a bank update
	 * 
	 * @param bankUpdate
	 *            - contains the old bank state and new bank state note: both
	 *            accounts provided are immutable
	 */
	public abstract void onBankUpdate(BankUpdate bankUpdate);

	/**
	 * Whenever an auction is occurring, the server will request a bid using
	 * this method and provide information about the auction as a part of the
	 * request
	 * 
	 * @param bidRequest
	 *            - auction metadata
	 */
	public abstract void onTradeRequest(BidReqeust bidRequest);

	/**
	 * Whenever another agent requests a trade either directly with this agent
	 * or to all agents, this method is invoked with the details of the trade.
	 * 
	 * @param tradeRequest
	 *            - from fields describe what this agent will receive and to
	 *            fields describe what it will give up
	 */
	public abstract void onNegotiateRequest(NegotiateRequest tradeRequest);
}
