package brown.generatepredictions;

public class SCPPIndHistLocalBidAgent {
	
	IValuation val = new AdditiveValuation(NUM_GOODS);
    IPredictionStrategy ps = new SCPPIndHist(new LocalBid);
    IBidStrategy bs = new LocalBid(ps.getPredictions);

}
