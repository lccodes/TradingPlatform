package messages;

public class Poll {
  public final double prob;
  public final int count;

  public Poll(double prob, int count) {
    this.prob = prob;
    this.count = count;
  }
  
  /**
   * Don't use me.
   */
  public Poll() {
    this.prob = 0;
    this.count = 0;
  }

}
