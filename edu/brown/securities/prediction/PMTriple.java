package brown.securities.prediction;

import brown.securities.mechanisms.lmsr.LMSRBackend;
import brown.securities.prediction.structures.PMNo;
import brown.securities.prediction.structures.PMYes;

public class PMTriple {
	public final LMSRBackend backend;
	public final PMYes yes;
	public final PMNo no;
	
	public final PMLedger ledgerYes;
	public final PMLedger ledgerNo;
	
	public PMTriple(LMSRBackend backend, PMYes yes, PMNo no, 
			PMLedger ledgerYes, PMLedger ledgerNo) {
		this.backend = backend;
		this.yes = yes;
		this.no = no;
		this.ledgerYes = ledgerYes;
		this.ledgerNo = ledgerNo;
	}

}
