package brown.assets.value;

public class FullType {
	public final SecurityType TYPE;
	public final Integer ID;
	
	public FullType() {
		this.TYPE = null;
		this.ID = null;
	}
	
	public FullType(SecurityType type, Integer ID) {
		this.TYPE = type;
		this.ID = ID;
	}
}
