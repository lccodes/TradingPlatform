package brown.securities;

import java.lang.reflect.InvocationTargetException;

import brown.securities.mechanisms.lmsr.PMBackend;
import brown.securities.prediction.PMLedger;
import brown.securities.prediction.PMTriple;
import brown.securities.prediction.structures.PMNo;
import brown.securities.prediction.structures.PMYes;

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
	
	public static PMTriple makeMarketMaker(Class<? extends PMBackend> backendClass, Integer yesID, 
			Integer noID, double b) {
		PMBackend backend;
		try {
			backend = backendClass.getConstructor().newInstance(b);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			System.out.println("[x] cannot construct " + backendClass);
			return null;
		}
		PMYes yes = new PMYes(yesID, backend);
		PMNo no = new PMNo(noID, backend);
		
		PMLedger ledgerYes = new PMLedger(yes, null);
		PMLedger ledgerNo = new PMLedger(no, ledgerYes);
		ledgerYes.setLedger(ledgerNo);
		
		return new PMTriple(backend, yes, no, ledgerYes, ledgerNo);
	}
}
