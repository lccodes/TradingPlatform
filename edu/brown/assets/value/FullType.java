package brown.assets.value;

public class FullType {
	public final TradeableType TYPE;
	public final Integer ID;
	
	public FullType() {
		this.TYPE = null;
		this.ID = null;
	}
	
	public FullType(TradeableType type, Integer ID) {
		this.TYPE = type;
		this.ID = ID;
	}
	
	@Override
	public String toString() {
		return "(" + TYPE + " " + ID + ")";
	}
	
	@Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((ID == null) ? 0 : ID.hashCode());
    result = prime * result + ((TYPE == null) ? 0 : TYPE.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FullType other = (FullType) obj;
    if (ID == null) {
      if (other.ID != null)
        return false;
    } else if (!ID.equals(other.ID))
      return false;
    if (TYPE != other.TYPE)
      return false;
    return true;
  }
	
	
}
