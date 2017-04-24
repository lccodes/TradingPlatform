package brown.securities.prediction.simulator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import brown.exceptions.AgentCreationException;
import brown.securities.lmsr.marketmakers.implementations.LiquiditySensitive;
import brown.securities.lmsr.marketmakers.implementations.LukeMM;
import brown.securities.mechanisms.lmsr.LMSR;
import brown.securities.mechanisms.lmsr.LMSRBackend;
import brown.securities.mechanisms.lmsr.strategies.ExperimentalAgent;
import brown.securities.mechanisms.lmsr.strategies.FarsightedInformed;
import brown.securities.mechanisms.lmsr.strategies.FarsightedUninformed;
import brown.securities.mechanisms.lmsr.strategies.ShortsightedInformed;
import brown.securities.mechanisms.lmsr.strategies.ShortsightedUninformed;

public class LMSRPlayground {
	private final List<ExperimentalAgent> AGENTS;
	private final BufferedWriter WRITER;
	private final ExperimentalServer SERVER;

	public LMSRPlayground(int sInformed, int sUninformed, int fInformed, int fUninformed)
			throws AgentCreationException, IOException {
		this.SERVER = new ExperimentalServer(2929);
		this.WRITER = new BufferedWriter(new FileWriter("experiment-" + new java.util.Date() + ".csv"));

		double prob = Math.random();
		this.WRITER.write("prob,sInf,sUninf,fInf,fUninf\n");
		this.WRITER.write(prob + "," + sInformed + "," + sUninformed + "," + fInformed + "," + fUninformed + "\n\n");
		this.WRITER.write("profit,b-a,prediction\n");
		int totalInformed = sInformed + fInformed;
		int numberOnes = (int) (prob * totalInformed);
		List<Integer> nums = new LinkedList<Integer>();
		for (int i = 0; i < totalInformed; i++) {
			if (numberOnes-- > 0) {
				nums.add(1);
			} else {
				nums.add(0);
			}
		}
		Collections.shuffle(nums);

		List<ExperimentalAgent> agents = new LinkedList<ExperimentalAgent>();
		for (int i = 0; i < sInformed; i++) {
			agents.add(new ShortsightedInformed("localhost", 2929, new ExperimentalSetup(), nums.get(0)));
			nums.remove(0);
		}
		for (int i = 0; i < sUninformed; i++) {
			agents.add(new ShortsightedUninformed("localhost", 2929, new ExperimentalSetup()));
		}

		for (int i = 0; i < fInformed; i++) {
			agents.add(new FarsightedInformed("localhost", 2929, new ExperimentalSetup(), nums.get(0)));
			nums.remove(0);
		}
		for (int i = 0; i < fUninformed; i++) {
			agents.add(new FarsightedUninformed("localhost", 2929, new ExperimentalSetup()));
		}

		this.AGENTS = agents;
	}
	
	public void simulate(int type, double param) throws IOException {
		Collections.shuffle(this.AGENTS);
		for (int i = 0; i < this.AGENTS.size(); i++) {
			this.AGENTS.get(i).setTime(i);
		}
		LMSRBackend backend = new LMSRBackend(0, param); //TODO: Tell it the truth and it can calc profit
		if (type == 1) {
			backend = new LiquiditySensitive(param);
		} else if (type == 2) {
			backend = new LukeMM(param);
		}
		LMSR yes = new LMSR(0,true, backend, false);
		LMSR no = new LMSR(1,false, backend, false);
		for (int i = 0; i < this.AGENTS.size(); i++) {
			this.SERVER.sendMarketUpdateNL(no);
			this.SERVER.sendMarketUpdateNL(yes);
		}
		this.WRITER.write(param + "," + yes.price()); //TODO: Profit?
	}
}