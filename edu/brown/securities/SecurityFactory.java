package brown.securities;

import brown.securities.prediction.PMBackend;
import brown.securities.prediction.PMNo;
import brown.securities.prediction.PMTriple;
import brown.securities.prediction.PMYes;

public final class SecurityFactory {

	public static PMTriple makePM(Integer yesID, Integer noID, double b) {
		PMBackend backend = new PMBackend(b);
		PMYes yes = new PMYes(yesID, backend);
		PMNo no = new PMNo(noID, backend);
		
		return new PMTriple(backend, yes, no);
	}
}
