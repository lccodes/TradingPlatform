package brown.securities.prediction.simulator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import brown.agent.library.ExperimentalAgent;
import brown.agent.library.FarsightedInformed;
import brown.agent.library.FarsightedUninformed;
import brown.agent.library.ShortsightedInformed;
import brown.agent.library.ShortsightedUninformed;
import brown.exceptions.AgentCreationException;
import brown.markets.LMSRBackend;
import brown.markets.LMSRServer;
import brown.markets.LiquiditySensitive;
import brown.markets.LukeMM;
import brown.server.library.ExperimentalServer;
import brown.setup.Logging;
import brown.setup.library.ExperimentalSetup;

public class LMSRPlayground {
	private final List<ExperimentalAgent> AGENTS;
	private final BufferedWriter WRITER;
	private final ExperimentalServer SERVER;
	private final double PROB;
	
	private final int PORT = 2917;

	public LMSRPlayground(int sInformed, int sUninformed, int fInformed, int fUninformed)
			throws AgentCreationException, IOException {
		this.SERVER = new ExperimentalServer(PORT);
		String datetime = new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date());
		this.WRITER = new BufferedWriter(new FileWriter("exp/" + sInformed + "_" + sUninformed + "_" + fInformed + "_"
				+ fUninformed + "-" + datetime + ".csv"));

		double prob = .35;
		double totalInformed = sInformed + fInformed;
		int numberOnes = (int) (prob * totalInformed);
		this.PROB = (((double) numberOnes)/((double)totalInformed));
		
		this.WRITER.write("prob,sInf,sUninf,fInf,fUninf\n");
		this.WRITER.write(PROB  + "," + sInformed + "," + sUninformed + "," + fInformed + "," + fUninformed + "\n\n");
		this.WRITER.write("profit,type,b-a,prediction\n");
		System.out.println(numberOnes + " given signal 1");
		
		List<Double> nums = new LinkedList<Double>();
		for (int i = 0; i < totalInformed; i++) {
			if (numberOnes-- > 0) {
				nums.add(.999);
			} else {
				nums.add(.001);
			}
		}
		Collections.shuffle(nums);

		List<ExperimentalAgent> agents = new LinkedList<ExperimentalAgent>();
		for (int i = 0; i < sInformed; i++) {
			agents.add(new ShortsightedInformed("localhost", PORT, new ExperimentalSetup(), nums.get(0)));
			nums.remove(0);
		}
		for (int i = 0; i < sUninformed; i++) {
			agents.add(new ShortsightedUninformed("localhost", PORT, new ExperimentalSetup()));
		}

		for (int i = 0; i < fInformed; i++) {
			agents.add(new FarsightedInformed("localhost", PORT, new ExperimentalSetup(), nums.get(0)));
			nums.remove(0);
		}
		for (int i = 0; i < fUninformed; i++) {
			agents.add(new FarsightedUninformed("localhost", PORT, new ExperimentalSetup()));
		}

		this.AGENTS = agents;
	}

	public void simulate(int type, double param) throws IOException {
		Collections.shuffle(this.AGENTS);
		for (int i = 0; i < this.AGENTS.size(); i++) {
			this.AGENTS.get(i).setTime(i);
		}
		this.SERVER.setBanks(1); //TODO: Fair way between MMs?
		LMSRBackend backend = new LMSRBackend(0, param); // TODO: Tell it the
															// truth and it can
															// calc profit
		if (type == 1) {
			backend = new LiquiditySensitive(param);
		} else if (type == 2) {
			backend = new LukeMM(param);
		}
		LMSRServer yes = new LMSRServer(0, true, backend, true);
		LMSRServer no = new LMSRServer(1, false, backend, true);
		this.SERVER.getManager().openTwoSided(yes);
		this.SERVER.getManager().openTwoSided(no);
		//for (int i = 0; i < this.AGENTS.size(); i++) {
			this.SERVER.sendMarketUpdateNL(no);
			this.SERVER.sendMarketUpdateNL(yes);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Logging.log("[+] woken: " + e.getMessage());
			}
		//}
		double cost = this.PROB >= .5 ? backend.yes : backend.no;
		this.WRITER.write((backend.getProfit()-cost) +","+type+","+param + "," + yes.price() + "\n"); // TODO: Profit?
		System.out.println("Final Price " + yes.price());
		this.WRITER.flush();
	}
	
	public void close() throws IOException {
		this.WRITER.close();
	}

	public static void main(String[] args) throws AgentCreationException, IOException {
		LMSRPlayground pg = new LMSRPlayground(49, 0, 0, 0);
//		for (double b = 1; b <= 20; b++) {
//			for (int i = 0; i < 10; i++) {
//				pg.simulate(0, b);
//			}
//		}
		for (double b = .01; b <= 1; b+=.01) {
			for (int i = 0; i < 10; i++) {
				pg.simulate(2, b);
			}
		}
		pg.close();
	}
}
