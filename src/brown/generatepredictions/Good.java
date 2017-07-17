package brown.generatepredictions;

public class Good {
	String name;
	
	public Good(){
		
	}
	
	@Override
	public boolean equals(Object o){
		if(this==o){
			return true;
		}
		if(!(o instanceof Good)){
			return false;
		}
		Good other = (Good) o;
		return (this.name.equals(other.name));
	}
}
