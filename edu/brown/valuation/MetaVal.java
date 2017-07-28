package brown.valuation;

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
  private Double stdDev; 
  
  /**
   * metadata for valuation that is necessary for SCPP and local-bidding agents.
   * @param dist
   * the distribution that valuations are drawn from. 
   * @param mean
   * the mean of valuations. 
   * @param stdDev
   * the standard deviation of valuations.
   */
  public MetaVal(DistributionType dist, Double mean, Double stdDev) {
    this.dist = dist; 
    this.mean = mean; 
    this.stdDev = stdDev; 
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
  public Double getStdDev() {
    return this.stdDev;
  }
  
}