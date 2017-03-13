package brown.lab.lab5;

import java.util.List;

import brown.messages.markets.GameReport;

public class LemonadeReport extends GameReport {
	private final List<Integer>[] SLOTS;
	
	public LemonadeReport() {
		this.SLOTS = null;
	}
	
	public LemonadeReport(List<Integer>[] slots) {
		this.SLOTS = slots;
	}
	
	public int getCount(int slot) {
		return this.SLOTS[slot].size();
	}

}
