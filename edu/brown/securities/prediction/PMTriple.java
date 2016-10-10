package brown.securities.prediction;

public class PMTriple {
	public final PMBackend backend;
	public final PMYes yes;
	public final PMNo no;
	
	public PMTriple(PMBackend backend, PMYes yes, PMNo no) {
		this.backend = backend;
		this.yes = yes;
		this.no = no;
	}

}
