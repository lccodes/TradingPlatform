package brown.messages.markets;


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
