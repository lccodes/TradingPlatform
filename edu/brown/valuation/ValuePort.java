package brown.valuation; 

import java.util.Set;
import java.util.function.Function;

import brown.assets.value.BasicType;

/**
 * this class receives valuations from an agnostic source (in this case the value generator) 
 * and converts them to native trading platform datatypes.
 * This is so the trading platform will not have to rely on datatypes from the value generator.
 * @author acoggins
 *
 */
public class ValuePort {
  
  private DistributionType dist; 
  private Function<Integer, Double> valFunction;
  private Boolean monotonic; 
  private Double valueScale;
  private Double baseVariance; 
  private Double expCovar; 
  private Integer numObservations; 
  private Integer bundleSizeMean; 
  private Double bundleSizeStdDev;
  
  public ValuePort(DistributionType dist, Function<Integer, Double> valFunction, Boolean monotonic, 
      Double valueScale, Double baseVariance, Double expCovar, Integer numObservations, 
      Integer bundleSizeMean, Double bundleSizeStdDev) {
    this.dist = dist; 
    this.valFunction = valFunction; 
    this.monotonic = monotonic; 
    this.valueScale = valueScale; 
    this.baseVariance = baseVariance; 
    this.expCovar = expCovar; 
    this.numObservations = numObservations; 
    this.bundleSizeMean = bundleSizeMean; 
    this.bundleSizeStdDev = bundleSizeStdDev; 
  }
  
  /**
   * for size dependent single good or all good case. 
   * @param dist
   * distribution type
   * @param valFunction
   * value function
   * @param valueScale
   * value scale. 
   */
  public ValuePort(DistributionType dist, Function<Integer, Double> valFunction, Double valueScale) {
    this.dist = dist; 
    this.valFunction = valFunction; 
    this.monotonic = false;
    this.valueScale = valueScale; 
    this.baseVariance = 0.0;
    this.expCovar = 0.0; 
    this.numObservations = 0;
    this.bundleSizeMean = 0;
    this.bundleSizeStdDev = 0.0;
  }
  
  /**
   * for size dependent some observations case.
   * @param dist
   * @param valFunction
   * @param valueScale
   * @param numObservations
   * @param bundleSizeMean
   * @param bundleSizeStdDev
   */
  public ValuePort(DistributionType dist, Function<Integer, Double> valFunction, Double valueScale, 
      Integer numObservations, Integer bundleSizeMean, Double bundleSizeStdDev) {
    this.dist = dist; 
    this.valFunction = valFunction; 
    this.monotonic = false;
    this.valueScale = valueScale; 
    this.baseVariance = 0.0;
    this.expCovar = 0.0; 
    this.numObservations = numObservations;
    this.bundleSizeMean = bundleSizeMean;
    this.bundleSizeStdDev = bundleSizeStdDev;
  }
  
  public Entry<BasicType, Double> getSingleValuation
  
  
}