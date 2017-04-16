import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.uzh.ifi.ce.mweiss.specval.bidlang.xor.SizeBasedUniqueRandomXOR;
import ch.uzh.ifi.ce.mweiss.specval.bidlang.xor.XORBid;
import ch.uzh.ifi.ce.mweiss.specval.bidlang.xor.XORValue;
import ch.uzh.ifi.ce.mweiss.specval.model.Bidder;
import ch.uzh.ifi.ce.mweiss.specval.model.Bundle;
import ch.uzh.ifi.ce.mweiss.specval.model.Good;
import ch.uzh.ifi.ce.mweiss.specval.model.UnsupportedBiddingLanguageException;
import ch.uzh.ifi.ce.mweiss.specval.model.mrm.MRMBidder;
import ch.uzh.ifi.ce.mweiss.specval.model.mrm.MRMLicense;
import ch.uzh.ifi.ce.mweiss.specval.model.mrm.MRMWorld;
import ch.uzh.ifi.ce.mweiss.specval.model.mrm.MultiRegionModel;
import ch.uzh.ifi.ce.mweiss.specvalopt.vcg.external.domain.Auction;
import ch.uzh.ifi.ce.mweiss.specvalopt.vcg.external.domain.AuctionResult;
import ch.uzh.ifi.ce.mweiss.specvalopt.vcg.external.domain.BidderPayment;
import ch.uzh.ifi.ce.mweiss.specvalopt.vcg.external.domain.Bids;
import ch.uzh.ifi.ce.mweiss.specvalopt.vcg.external.domain.Payment;
import ch.uzh.ifi.ce.mweiss.specvalopt.vcg.external.domain.XORAllocation;
import ch.uzh.ifi.ce.mweiss.specvalopt.vcg.external.domain.mechanisms.AuctionMechanism;
import ch.uzh.ifi.ce.mweiss.specvalopt.vcg.external.vcg.XORVCGAuction;


public class SpecValGenerator {	
	public int numberOfBidders;
	public int numberOfValuationsPerBidder;
	public int bundleSizeMean;
	public double bundleSizeStdev;
	public double valueScale; // value * valueScale -- scale numbers by a factor
	
	public int numberOfGlobalBidders;
	public int numberOfLocalBidders;
	public int numberOfRegionalBidders;
		
	public MultiRegionModel multiRegionModel;
	public MRMWorld mrmWorld;
	
	public Set<MRMLicense> licenses; // Goods are licenses
	public Map<Long, MRMLicense> licenseIdToLicense;
	public Set<String> goods;
	
	public List<MRMBidder> population;
	public Map<Long, Bidder<MRMLicense>> bidderIdToBidder;
	public Map<Long, SizeBasedUniqueRandomXOR<MRMLicense>> bidderIdToValuation;
	public Bids<MRMLicense> allBidderValuations;
	
	public Auction auction;
	public AuctionMechanism am;
	public AuctionResult auctionResult;
	
//	public SpecValGenerator(int numberOfBidders, int numberOfValuationsPerBidder) {
//		int bundleSizeMean = 3; 
//		double bundleSizeStdev = 2.0; 
//		double valueScale = 1.0;
//		
//		// Define bidder parameters
//		this.numberOfBidders = numberOfBidders;
//		this.numberOfGlobalBidders = numberOfBidders;
//		this.numberOfLocalBidders = 0;
//		this.numberOfRegionalBidders = 0;
//
//		// Define valuation parameters
//		this.numberOfValuationsPerBidder = numberOfValuationsPerBidder;
//		this.bundleSizeMean = 3;
//		this.bundleSizeStdev = 2.0;
//				
//		// Make World
//		this.multiRegionModel = new MultiRegionModel();
//		multiRegionModel.setNumberOfGlobalBidders(numberOfGlobalBidders);
//		multiRegionModel.setNumberOfLocalBidders(numberOfLocalBidders);
//		multiRegionModel.setNumberOfRegionalBidders(numberOfRegionalBidders);
//		this.mrmWorld = multiRegionModel.createWorld();
//		
//		// Make bidders, which generates valuations
//		this.population = multiRegionModel.createNewPopulation();
//		
//		// Initialize
//		this.bidderIdToBidder = new HashMap<>();
//		this.bidderIdToValuation = new HashMap<>();
//		this.allBidderValuations = new Bids<>();
//	}
	

//	public SpecValGenerator(int numberOfBidders, int numberOfValuationsPerBidder, 
//			int bundleSizeMean, double bundleSizeStdev, double valueScale) {
//		// Define bidder parameters
//		this.numberOfBidders = numberOfBidders;
//		this.numberOfGlobalBidders = numberOfBidders;
//		this.numberOfLocalBidders = 0;
//		this.numberOfRegionalBidders = 0;
//
//		// Define valuation parameters
//		this.numberOfValuationsPerBidder = numberOfValuationsPerBidder;
//		this.bundleSizeMean = 3;
//		this.bundleSizeStdev = 2.0;
//				
//		// Make World
//		this.multiRegionModel = new MultiRegionModel();
//		multiRegionModel.setNumberOfGlobalBidders(numberOfGlobalBidders);
//		multiRegionModel.setNumberOfLocalBidders(numberOfLocalBidders);
//		multiRegionModel.setNumberOfRegionalBidders(numberOfRegionalBidders);
//		this.mrmWorld = multiRegionModel.createWorld();
//		
//		// Make bidders, which generates valuations
//		this.population = multiRegionModel.createNewPopulation();
//		
//		// Initialize
//		this.bidderIdToBidder = new HashMap<>();
//		this.bidderIdToValuation = new HashMap<>();
//		this.allBidderValuations = new Bids<>();
//	}
	
	public SpecValGenerator(int numberOfBidders, int numberOfValuationsPerBidder) {
		int bundleSizeMean = 3; 
		double bundleSizeStdev = 2.0; 
		double valueScale = 1E-6;
		finishConstructor(numberOfBidders, numberOfValuationsPerBidder, 
				bundleSizeMean, bundleSizeStdev, valueScale);
	}
	
	public SpecValGenerator(int numberOfBidders, int numberOfValuationsPerBidder, 
			int bundleSizeMean, double bundleSizeStdev, double valueScale) {
		finishConstructor(numberOfBidders, numberOfValuationsPerBidder, 
				bundleSizeMean, bundleSizeStdev, valueScale);
	}
	
	private void finishConstructor(int numberOfBidders, int numberOfValuationsPerBidder, 
			int bundleSizeMean, double bundleSizeStdev, double valueScale) {
		this.numberOfBidders = numberOfBidders;
		this.numberOfValuationsPerBidder = numberOfValuationsPerBidder;
		this.bundleSizeMean = bundleSizeMean;
		this.bundleSizeStdev = bundleSizeStdev;
		this.valueScale = valueScale;
		
		// Define bidder parameters
		this.numberOfGlobalBidders = numberOfBidders;
		this.numberOfLocalBidders = 0;
		this.numberOfRegionalBidders = 0;
				
		// Make World
		this.multiRegionModel = new MultiRegionModel();
		multiRegionModel.setNumberOfGlobalBidders(numberOfGlobalBidders);
		multiRegionModel.setNumberOfLocalBidders(numberOfLocalBidders);
		multiRegionModel.setNumberOfRegionalBidders(numberOfRegionalBidders);
		this.mrmWorld = multiRegionModel.createWorld();
		
		// Make bidders, which generates valuations
		this.population = multiRegionModel.createNewPopulation();
		
		// Initialize
		this.bidderIdToBidder = new HashMap<>();
		this.bidderIdToValuation = new HashMap<>();
		this.allBidderValuations = new Bids<>();
	}
	
	public void makeValuations() throws UnsupportedBiddingLanguageException {
		// Generate valuations
		for (Bidder<MRMLicense> bidder : this.population) {
			this.bidderIdToBidder.put(bidder.getId(), bidder);		
			// Make and store bidder valuations
    	    SizeBasedUniqueRandomXOR<MRMLicense> xorBids = bidder.getValueFunction(SizeBasedUniqueRandomXOR.class);
    	    //xorBids.setDefaultDistribution();
    	    xorBids.setDistribution(this.bundleSizeMean, this.bundleSizeStdev, this.numberOfValuationsPerBidder);
    	    this.bidderIdToValuation.put(bidder.getId(), xorBids);
			xorBids = this.bidderIdToValuation.get(bidder.getId());
			Iterator<? extends XORValue<MRMLicense>> bidsIterator = xorBids.iterator();
			Set <XORValue<MRMLicense>> allBids = new HashSet<XORValue<MRMLicense>>();
			while(bidsIterator.hasNext()) {
				XORValue<MRMLicense> bid = bidsIterator.next();
				double scaledBidValue = this.valueScale * bid.value().doubleValue();
				bid.setValue(new BigDecimal(scaledBidValue));
				allBids.add(bid);
			}
			this.allBidderValuations.addBid(new XORBid.Builder<MRMLicense>(bidder, allBids).build());
		}
		
		this.collectLicenseInformation();
	}
	
	public void collectLicenseInformation() {
		this.licenseIdToLicense = new HashMap<>(); 
		for (Bidder<MRMLicense> bidder : this.population) {
			this.licenses = (Set<MRMLicense>) bidder.getWorld().getLicenses();
			for(MRMLicense l : licenses) {
				licenseIdToLicense.put(l.getId(), l);
			}
			break;
		}
		this.goods = convertLicensesToGoods(this.licenses);
	}
	
	// -------------------------------------------------------------------------
	// Solve for welfare-maximizing auctions
	// -------------------------------------------------------------------------
	
	public AuctionResult runVCGWithGeneratedValues() {
		this.auction = new Auction(this.allBidderValuations, this.mrmWorld.getLicenses());
		this.am = new XORVCGAuction<>(this.auction);
		this.auctionResult = this.am.getAuctionResult();
		return this.auctionResult;
	}
	
	public AuctionResult runVCGWithGivenBids(Map<String, Map<Set<String>, Double>> purportedTypes) {
		Bids<MRMLicense> givenAllBidsMadeByBidders = convertAllBiddersPurportedTypeToBids(purportedTypes);
		this.auction = new Auction(givenAllBidsMadeByBidders, this.mrmWorld.getLicenses());
		this.am = new XORVCGAuction<>(this.auction);
		this.auctionResult = this.am.getAuctionResult();
		return this.auctionResult;
	}
	
	// -------------------------------------------------------------------------
	// Queries
	// -------------------------------------------------------------------------
	
	public double valueQuery(long bidderId, SortedSet<String> bundle) {
		Bidder<MRMLicense> bidder = this.bidderIdToBidder.get(bidderId);
		Bundle<MRMLicense> convertedBundle = convertSimpleBundleToBundle(bundle);
		double value = bidder.calculateValue(convertedBundle).doubleValue();
		return value;
	}
	
	// -------------------------------------------------------------------------
	// Conversions
	// -------------------------------------------------------------------------
	
	public Set<String> convertLicensesToGoods(Set<MRMLicense> _licenses) {
		Set<String> licensesToGoods = new HashSet<String>();
		for (MRMLicense l : _licenses) {
			String goodId = String.valueOf(l.getId());
			licensesToGoods.add(goodId);
		}
		return licensesToGoods;
	}
	
	public Bundle<MRMLicense> convertSimpleBundleToBundle(Set<String> simpleBundle) {
		Bundle<MRMLicense> convertedBundle = new Bundle<MRMLicense>();
		for (String g : simpleBundle) {
			Long goodId = Long.parseLong(g);
			MRMLicense l = this.licenseIdToLicense.get(goodId);
			convertedBundle.add(l);
		}
		return convertedBundle;
	}
	
	public Set<String> convertBundleToSimpleBundle(Bundle<MRMLicense> bundle) {
		Set<String> simpleBundle = new TreeSet<String>();
		for (MRMLicense l : bundle) {
			simpleBundle.add(String.valueOf(l.getId()));
		}
		return simpleBundle;
	}
		
	public XORValue<MRMLicense> convertOneBidToXORValue(Set<String> bundle, double purportedValue) {
		Bundle<MRMLicense> myBundle = convertSimpleBundleToBundle(bundle);
		BigDecimal myValue = new BigDecimal(purportedValue);
		XORValue<MRMLicense> myXorAtomicBid = new XORValue<MRMLicense>(myBundle, myValue);
		return myXorAtomicBid;
	}
	
	public Map<Set<String>, Double> convertXORValueToOneBid(XORValue<MRMLicense> xorValue) {
		Map<Set<String>, Double> bid = new HashMap<Set<String>, Double>();
		double value = xorValue.value().doubleValue();
		Set<String> simpleBundle = new TreeSet<String>();
		simpleBundle = convertBundleToSimpleBundle(xorValue.getLicenses());
		bid.put(simpleBundle, value);
		return bid;
	}
	
	public Set<XORValue<MRMLicense>> convertManyBidsToXORValue(Map<Set<String>, Double> valuations) {
		Set <XORValue<MRMLicense>> allBids = new HashSet<XORValue<MRMLicense>>();
		for (Set<String> bundle : valuations.keySet()) {
			double value = (double)valuations.get(bundle);
			XORValue<MRMLicense> myXorAtomicBid = convertOneBidToXORValue(bundle, value);
			allBids.add(myXorAtomicBid);
		}
		return allBids;
	}
	
	public Bids<MRMLicense> convertAllBiddersPurportedTypeToBids(Map<String, Map<Set<String>, Double>> purportedTypes) {
		Bids<MRMLicense> allBidsMadeByBidders = new Bids<>();
		for (String id : purportedTypes.keySet()) {
			Long bidderId = Long.parseLong(id);
			Map<Set<String>, Double> valuations = purportedTypes.get(id);
			Set <XORValue<MRMLicense>> allBids = convertManyBidsToXORValue(valuations);
			Bidder<MRMLicense> bidder = this.bidderIdToBidder.get(bidderId);
			allBidsMadeByBidders.addBid(new XORBid.Builder<MRMLicense>(bidder, allBids).build());
		}
		return allBidsMadeByBidders;
	}
	
	public Map<String, Map<Set<String>, Double>> convertAllBidsToSimpleBids(Bids<MRMLicense> everyBiddersBids) {
		Map<String, Map<Set<String>, Double>> everyBiddersSimpleBids = new HashMap<String, Map<Set<String>, Double>>();
		for (XORBid<MRMLicense> xorBid : everyBiddersBids) {
			Map<Set<String>, Double> simpleBids = new HashMap<Set<String>, Double>();
			Bidder<MRMLicense> bidder = xorBid.getBidder();
			String bidderId = Long.toString(bidder.getId());
			List<XORValue<MRMLicense>> xorValues = xorBid.getValues();
			for (XORValue<MRMLicense> xorValue : xorValues) {
				Map<Set<String>, Double> simpleBid = convertXORValueToOneBid(xorValue);
				for (Set<String> k : simpleBid.keySet()) {
					simpleBids.put(k, simpleBid.get(k));
				}
			}
			everyBiddersSimpleBids.put(bidderId, simpleBids);
		}
		return everyBiddersSimpleBids;
	}
	
	public static Map<String, SortedSet<String>> getVcgAllocationSimpleBid(AuctionResult auctionResult) {
		Map<String, SortedSet<String>> simpleBidAllocation = new HashMap<String, SortedSet<String>>();
		XORAllocation<MRMLicense> alloc = auctionResult.getAllocation();
		Set<Bidder<MRMLicense>> winners = alloc.getWinners();
		Set<Bidder<MRMLicense>> bidders = (Set<Bidder<MRMLicense>>) alloc.getBidders();
		for (Bidder<MRMLicense> bidder : bidders) {
			if (winners.contains(bidder)) {
				Set <Good> goods = alloc.getAllocation(bidder).getGoods();
				SortedSet <String> bundle = new TreeSet<String>();
				for (Good g : goods) {
					bundle.add(Long.toString(g.getId()));
				}
				simpleBidAllocation.put(String.valueOf(bidder.getId()), bundle);
			} else {
				SortedSet <String> bundle = new TreeSet<String>();
				simpleBidAllocation.put(String.valueOf(bidder.getId()), bundle);
			}
		}
		return simpleBidAllocation;
	}
	
	public static Map<String, Double> getVcgPaymentSimpleBid(AuctionResult auctionResult) {
		Map<String, Double> simplePayment = new HashMap<String, Double>();
		XORAllocation<MRMLicense> alloc = auctionResult.getAllocation();
		Set<Bidder<MRMLicense>> winners = alloc.getWinners();
		Set<Bidder<MRMLicense>> bidders = (Set<Bidder<MRMLicense>>) alloc.getBidders();
		Payment<MRMLicense> payment = auctionResult.getPayment();
		for (Bidder<MRMLicense> bidder : bidders) {
			if (winners.contains(bidder)) {
				BidderPayment bidderPayment = payment.paymentOf(bidder);
				simplePayment.put(String.valueOf(bidder.getId()), bidderPayment.getAmount());
			} else {
				simplePayment.put(String.valueOf(bidder.getId()), 0.0);
			}
		}
		return simplePayment;
	}
	
	
	// -------------------------------------------------------------------------
	// Terminal output
	// -------------------------------------------------------------------------
	
	public static void printVcgResults(AuctionResult auctionResult) {
		XORAllocation<MRMLicense> alloc = auctionResult.getAllocation();
		Payment<MRMLicense> payment = auctionResult.getPayment();
		
		System.out.println("----------------------------------------");
		System.out.println("VCG Results");
		System.out.println("----------------------------------------");
		
		System.out.println("Total Welfare: " + alloc.getTotalAllocationValue());
		System.out.println("Total Revenue: " + payment.getTotalPayments());
		
		Set<Bidder<MRMLicense>> winners = alloc.getWinners();
		for(Bidder<MRMLicense> bidder : winners) {
			System.out.print("Bidder " + bidder.getId() + " : ");
			System.out.print("Allocation: ");
			Set <Good> goods = alloc.getAllocation(bidder).getGoods();
			for(Good g : goods) {
				System.out.print(g.getId() + " ");
			}
			System.out.println("");
			BidderPayment bidderPayment = payment.paymentOf(bidder);
			System.out.println("Payments: " + bidderPayment.getAmount());
		}
	}
	
	public static void printValuations(Bids<MRMLicense> allBidderValuations) {
		for (XORBid<MRMLicense> bidderValuation : allBidderValuations) {
			System.out.println("Bidder: " + bidderValuation.getBidder().getId());
			for (XORValue<MRMLicense> x : bidderValuation.getValues()) {
				System.out.println("Bundle: " + x.getLicenses().itemIds(",")
						+ "\t Value: " + x.value().doubleValue());
			}
		}
	}
	
	public static void printValuations(Map<String, Map<Set<String>, Double>> allSimpleBids) {
		SortedSet<String> bidderIds = new TreeSet<String>();
		bidderIds.addAll(allSimpleBids.keySet());
		for (String bidderId : bidderIds) {
			System.out.println("Bidder: " + bidderId);
			Map<Set<String>, Double> simpleBids = allSimpleBids.get(bidderId);
			for (Set<String> bundle : simpleBids.keySet()) {
				System.out.print("Bundle: ");
				SortedSet<String> sortedBundle = new TreeSet<String>();
				sortedBundle.addAll(bundle);
				for (String s : sortedBundle) {
					System.out.print(s + " ");
				}
				System.out.print(":\t Value: " + simpleBids.get(bundle) + "\n");
			}
		}
	}
	
	// -------------------------------------------------------------------------
	// Simple tests
	// -------------------------------------------------------------------------
	
	public static void myXorBidConstructTest() {
		System.out.println("----------------------------------------");
		System.out.println("myXorBidConstructTest()");
		System.out.println("----------------------------------------");
		
		// Make Model
		MultiRegionModel multiRegionModel = new MultiRegionModel();
		multiRegionModel.setNumberOfGlobalBidders(2);
		multiRegionModel.setNumberOfLocalBidders(0);
		multiRegionModel.setNumberOfRegionalBidders(0);
		
		// Make world and get number of goods (licenses) involved
		MRMWorld mrmWorld = multiRegionModel.createWorld();
		
		// Apparently, the world these licenses belong to are different
		// from the world the bidders belong to....
//		SortedSet<MRMLicense> licenses = mrmWorld.getLicenses();
//		// Make it easy to get a license
//		Map <Long, MRMLicense> indexToLicense = new HashMap<>();
//		for(MRMLicense l : licenses) {
//			indexToLicense.put(l.getId(), l);
//		}
		
		// Make bidders, which generates valuations
		List<MRMBidder> population = multiRegionModel.createNewPopulation();
		
		Map<Long, Bidder<MRMLicense>> bidders = new HashMap<>();
		Map<Long, SizeBasedUniqueRandomXOR<MRMLicense>> bidderValuations = new HashMap<>();
		Bids<MRMLicense> allBidsMadeByBidders = new Bids<>();
		for (Bidder<MRMLicense> bidder : population) {
			Set<MRMLicense> licenses = (Set<MRMLicense>) bidder.getWorld().getLicenses();
			
			// Make it easy to get a license
			Map <Long, MRMLicense> indexToLicense = new HashMap<>();
			for(MRMLicense l : licenses) {
				indexToLicense.put(l.getId(), l);
			}
			
			// Check which worlds licenses and bidders belong to.
			// They must match!  Otherwise, the code will throw a fit.
			// System.out.println(bidder.getWorldId());
			// System.out.println(indexToLicense.get(new Long(1)).getWorldId());
			
			// Make an XOR bid
			// First, the bundle of goods to bid on
			Bundle<MRMLicense> myBundle1 = new Bundle<MRMLicense>();
			myBundle1.add(indexToLicense.get(new Long(1)));
			myBundle1.add(indexToLicense.get(new Long(2)));
			myBundle1.add(indexToLicense.get(new Long(3)));
			// Purported value of bundle
			BigDecimal myValue1 = new BigDecimal(1.0 + bidder.getId());
			// The actual atomic bid
			XORValue<MRMLicense> myBid1 = new XORValue<MRMLicense>(myBundle1, myValue1);
			
			// Make another XOR bid
			// First, the bundle of goods to bid on
			Bundle<MRMLicense> myBundle2 = new Bundle<MRMLicense>();
			myBundle2.add(indexToLicense.get(new Long(4)));
			myBundle2.add(indexToLicense.get(new Long(5)));
			myBundle2.add(indexToLicense.get(new Long(6)));
			// Purported value of bundle
			BigDecimal myValue2 = new BigDecimal(1.0 + 1.0 - bidder.getId());
			// The actual atomic bid
			XORValue<MRMLicense> myBid2 = new XORValue<MRMLicense>(myBundle2, myValue2);
			
			// Add the bid to the SortedSet of all bids
			Set <XORValue<MRMLicense>> allBids = new HashSet<XORValue<MRMLicense>>();
			allBids.add(myBid1);
			allBids.add(myBid2);
			allBidsMadeByBidders.addBid(new XORBid.Builder<MRMLicense>(bidder, allBids).build());			
		}
		printValuations(allBidsMadeByBidders);
		
		// Run VCG
		Auction auction = new Auction(allBidsMadeByBidders, mrmWorld.getLicenses());
		AuctionMechanism am = new XORVCGAuction<>(auction);
		AuctionResult auctionResult = am.getAuctionResult();
		printVcgResults(auctionResult);
	}
		
	public static void myVcgTest() throws UnsupportedBiddingLanguageException {
		System.out.println("----------------------------------------");
		System.out.println("myVcgTest()");
		System.out.println("----------------------------------------");
		// Define constants
		// Population
		int numberOfGlobalBidders = 2;
		int numberOfLocalBidders = 0;
		int numberOfRegionalBidders = 0;
		// Number of XOR bids to describe valuations
		int bidsPerBidder = 2;
		
		// Make World
		MultiRegionModel multiRegionModel = new MultiRegionModel();
		multiRegionModel.setNumberOfGlobalBidders(numberOfGlobalBidders);
		multiRegionModel.setNumberOfLocalBidders(numberOfLocalBidders);
		multiRegionModel.setNumberOfRegionalBidders(numberOfRegionalBidders);
		MRMWorld mrmWorld = multiRegionModel.createWorld();
	
		// Make bidders, which generates valuations
		List<MRMBidder> population = multiRegionModel.createNewPopulation();

		// In case we want to quickly refer to licenses
		Map <Long, MRMLicense> indexToLicense = new HashMap<>();
		Set<MRMLicense> licenses; 
		for (Bidder<MRMLicense> bidder : population) {
			licenses = (Set<MRMLicense>) bidder.getWorld().getLicenses();
			for(MRMLicense l : licenses) {
				indexToLicense.put(l.getId(), l);
			}
			break;
		}
		// They're all integers, from 0 to 98
		ArrayList<Long> lincenseIndices = new ArrayList(indexToLicense.keySet());
		lincenseIndices.sort(null);
		System.out.println("Number of licenses: " + lincenseIndices.size());
		System.out.println("Min index: " + lincenseIndices.toArray()[0]);
		System.out.println("Max index: " + lincenseIndices.toArray()[lincenseIndices.size() - 1]);
		System.out.println("Licenses: " + Arrays.toString(lincenseIndices.toArray()));
		
		// Store bidder information
		Map<Long, Bidder<MRMLicense>> bidders = new HashMap<>();
		Map<Long, SizeBasedUniqueRandomXOR<MRMLicense>> bidderValuations = new HashMap<>();
		Bids<MRMLicense> allBidderValuations = new Bids<>();
		
		// Generate valuations
		for (Bidder<MRMLicense> bidder : population) {
			bidders.put(bidder.getId(), bidder);		
			// Make and store bidder valuations
    	    SizeBasedUniqueRandomXOR<MRMLicense> xorBids = bidder.getValueFunction(SizeBasedUniqueRandomXOR.class);
    	    xorBids.setDistribution(3, 2, bidsPerBidder);
			bidderValuations.put(bidder.getId(), xorBids);
			xorBids = bidderValuations.get(bidder.getId());
			Iterator<? extends XORValue<MRMLicense>> bidsIterator = xorBids.iterator();
			Set <XORValue<MRMLicense>> allBids = new HashSet<XORValue<MRMLicense>>();
			while(bidsIterator.hasNext()) {
				XORValue<MRMLicense> bid = bidsIterator.next();
				allBids.add(bid);
			}
			allBidderValuations.addBid(new XORBid.Builder<MRMLicense>(bidder, allBids).build());
		}
		
		// Print Valuations
		printValuations(allBidderValuations);
		
		// Solve VCG
		Auction auction = new Auction(allBidderValuations, mrmWorld.getLicenses());
		AuctionMechanism am = new XORVCGAuction<>(auction);
		AuctionResult auctionResult = am.getAuctionResult();
		printVcgResults(auctionResult);
	}
	
	public static void myXorBidConstructTest2() throws UnsupportedBiddingLanguageException {
		int numberOfBidders = 2;
		int numberOfValuationsPerBidder = 20;
		SpecValGenerator generator = new SpecValGenerator(numberOfBidders, numberOfValuationsPerBidder);
		for (Bidder b : generator.population) {
			generator.bidderIdToBidder.put(b.getId(), b);
		}
		generator.collectLicenseInformation();
		
		Map<String, Map<Set<String>, Double>> purportedTypes = new HashMap<String, Map<Set<String>, Double>> ();
		
		Map<Set<String>, Double> val1 = new HashMap<Set<String>, Double>();
		Set<String> bundle1a = new HashSet<String>();
		bundle1a.add("1");
		bundle1a.add("2");
		bundle1a.add("3");
		val1.put(bundle1a, 1.0);
		Set<String> bundle1b = new HashSet<String>();
		bundle1b.add("4");
		bundle1b.add("5");
		bundle1b.add("6");
		val1.put(bundle1b, 2.0);
		
		Map<Set<String>, Double> val2 = new HashMap<Set<String>, Double>();
		Set<String> bundle2a = new HashSet<String>();
		bundle2a.add("1");
		bundle2a.add("2");
		bundle2a.add("3");
		val2.put(bundle2a, 2.0);
		Set<String> bundle2b = new HashSet<String>();
		bundle2b.add("4");
		bundle2b.add("5");
		bundle2b.add("6");
		val2.put(bundle2b, 1.0);
		
		purportedTypes.put("0", val1);
		purportedTypes.put("1", val2);
		
		Bids<MRMLicense> allBidsMadeByBidders = generator.convertAllBiddersPurportedTypeToBids(purportedTypes);
		
		printValuations(allBidsMadeByBidders);
		
		generator.runVCGWithGivenBids(purportedTypes);
		generator.printVcgResults(generator.auctionResult);
	}
	
	public static void myVcgTest2() throws UnsupportedBiddingLanguageException {
		int numberOfBidders = 5;
		// How many atomic bids to generate
		int numberOfValuationsPerBidder = 10;
		// Mean bundle size of an XOR value
		// There are 98 goods.
		int bundleSizeMean = 98/2;
		// Standard deviation of bundle size.
		// Distribution is Gaussian.
		double bundleSizeStdev = 98/4;
		// Value multiplier.  To avoid solver problems
		// Eg: Exception in thread "main" edu.harvard.econcs.jopt.solver.MIPException: 
		// Value (4.731434842153559E9) must be less than MIP.MAX_VALUE: 536870910
		// value = generatedValue * valueScale
		double valueScale = 1E-6;
		
		SpecValGenerator generator = new SpecValGenerator(numberOfBidders, numberOfValuationsPerBidder,
				bundleSizeMean, bundleSizeStdev, valueScale);
		generator.makeValuations();
		
		// BidderIds are strings.
		// Goods are labeled 0 - 97.  All strings.
		// Bundles are sets of strings.
		// BidderId -> Bundle -> Value
		Map<String, Map<Set<String>, Double>> allSimpleBids = generator.convertAllBidsToSimpleBids(generator.allBidderValuations);
		//generator.printValuations(generator.allBidderValuations);
		generator.printValuations(allSimpleBids);
		generator.runVCGWithGivenBids(allSimpleBids);
		//generator.printVcgResults(generator.auctionResult);
		// BidderId -> Bundle
		Map<String, SortedSet<String>> simpleAllocation = getVcgAllocationSimpleBid(generator.auctionResult);
		// BidderId -> Payment
		Map<String, Double> simplePayment = getVcgPaymentSimpleBid(generator.auctionResult);
		for (String bidderId : simpleAllocation.keySet()) {
			String bundle = new String();
			for (String s : simpleAllocation.get(bidderId)) {
				bundle += s + " ";
			}
			System.out.println("Bidder " + bidderId + ": "
					+ "\tBundle: " + bundle + "\tPrice: " + simplePayment.get(bidderId));
		}
	}
	
	public static void main(String[] args) throws UnsupportedBiddingLanguageException {
		//myXorBidConstructTest();
		//myVcgTest();
		//myXorBidConstructTest2();
		myVcgTest2();
	}
}
