package brown.securities.prediction;

public class PMTriple {
	public final PMBackend backend;
	public final PMYes yes;
	public final PMNo no;
	
	public final PMLedger ledgerYes;
	public final PMLedger ledgerNo;
	
	public PMTriple(PMBackend backend, PMYes yes, PMNo no, 
			PMLedger ledgerYes, PMLedger ledgerNo) {
		this.backend = backend;
		this.yes = yes;
		this.no = no;
		this.ledgerYes = ledgerYes;
		this.ledgerNo = ledgerNo;
	}

}
