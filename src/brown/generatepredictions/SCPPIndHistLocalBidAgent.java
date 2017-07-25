package brown.generatepredictions;

import brown.interfaces.IBidStrategy;
import brown.interfaces.IValuation;

public class SCPPIndHistLocalBidAgent {
	
	IValuation val = new AdditiveValuation(NUM_GOODS);
    IPredictionStrategy ps = new SCPPIndHist(new LocalBid);
    IBidStrategy bs = new LocalBid(ps.getPredictions);

}
