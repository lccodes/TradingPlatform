package brown.lab.lab5;

import brown.messages.markets.GameReport;

public class LemonadeReport extends GameReport {
	private final int[] SLOTS;
	
	public LemonadeReport() {
		this.SLOTS = null;
	}
	
	public LemonadeReport(int[] slots) {
		this.SLOTS = slots;
	}
	
	public int getCount(int slot) {
		return this.SLOTS[slot-1];
	}

}
