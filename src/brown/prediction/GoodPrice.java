package brown.prediction;

import java.util.AbstractMap.SimpleEntry;

import brown.assets.value.FullType;

public class GoodPrice {
  
  private SimpleEntry<FullType, Double> entry;
  
  public GoodPrice(FullType good, Double price) {
    this.entry = new SimpleEntry<FullType, Double>(good, price);
  }
  
  public FullType getGood() {
    return entry.getKey();
  }
  
  public Double getPrice() {
    return entry.getValue();
  }
  
  public void setPrice(Double newPrice) {
    entry.setValue(newPrice);
  }

  @Override
  public String toString() {
    return "GoodPrice [entry=" + entry + "]";
  }
  
  
}