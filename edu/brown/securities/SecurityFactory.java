package brown.securities;

import brown.securities.prediction.PMBackend;
import brown.securities.prediction.PMLedger;
import brown.securities.prediction.PMNo;
import brown.securities.prediction.PMTriple;
import brown.securities.prediction.PMYes;

public final class SecurityFactory {

	public static PMTriple makePM(Integer yesID, Integer noID, double b) {
		PMBackend backend = new PMBackend(b);
		PMYes yes = new PMYes(yesID, backend);
		PMNo no = new PMNo(noID, backend);
		
		PMLedger ledgerYes = new PMLedger(yes, null);
		PMLedger ledgerNo = new PMLedger(no, ledgerYes);
		ledgerYes.setLedger(ledgerNo);
		
		return new PMTriple(backend, yes, no, ledgerYes, ledgerNo);
	}
}
