package messages;

public class Poll {
  public final int countYes;
  public final int countNo;

  public Poll(int countOne, int countTwo) {
    this.countYes = countOne;
    this.countNo = countTwo;
  }
  
  /**
   * Don't use me.
   */
  public Poll() {
    this.countYes = 0;
    this.countNo = 0;
  }

}
