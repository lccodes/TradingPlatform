package brown.valuation;

import java.util.function.Function;

/**
 * gives metadata about the valuation, which involves the distribution that the
 * valuations are drawn from. 
 * 
 * @author acoggins
 *
 */
public class MetaVal {
  
  private DistributionType dist; 
  private Double mean; 
  private Double variance; 
  private Function<Integer, Double> valFunction;
  private Boolean monotonic;
  private Double scale;
  
  /**
   * metadata for valuation that is necessary for SCPP and local-bidding agents.
   * @param dist
   * the distribution that valuations are drawn from. 
   * @param mean
   * the mean of valuations. 
   * @param stdDev
   * the standard deviation of valuations.
   */
  public MetaVal(DistributionType dist, Double mean, Double variance, Function<Integer, Double> 
  valFunction, Boolean isMonotonic, Double valueScale) {
    this.dist = dist; 
    this.mean = mean; 
    this.variance = variance; 
    this.valFunction = valFunction; 
    this.monotonic = isMonotonic; 
    this.scale = valueScale;
  }
  
  /**
   * gets the distribution type
   * @return
   */
  public DistributionType getType() {
    return this.dist;
  }
  
  /**
   * gets the distribution mean. 
   * @return
   */
  public Double getMean() {
    return this.mean; 
  }
  
  /**
   * gets the distribution standard deviation.
   * @return
   */
  public Double getVariance() {
    return this.variance;
  }
  
  public Function<Integer, Double> getValFunction() {
    return this.valFunction; 
  }
  
  public Boolean getMonotonic() {
    return this.monotonic; 
  }
  
  public Double getScale() {
    return this.scale;
  }
  
}