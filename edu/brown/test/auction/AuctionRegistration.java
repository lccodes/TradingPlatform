package brown.test.auction;

import brown.messages.Registration;

public class AuctionRegistration extends Registration {

  private final double value;

  public AuctionRegistration() {
    super(null);
    this.value = -1;
  }

  public AuctionRegistration(Integer id, double value) {
    super(id);
    this.value = value;
  }

  public double getValue() {
    return this.value;
  }
}
