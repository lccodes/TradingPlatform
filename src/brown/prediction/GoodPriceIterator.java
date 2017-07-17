package brown.prediction; 

import java.util.Iterator;

public class GoodPriceIterator implements Iterable<GoodPrice>, Iterator<GoodPrice>{
  
  private GoodPrice[] priceArray;
  private int index;
  
  public GoodPriceIterator(PredictionVector valSet) {
    this.priceArray = valSet.toArray(); 
    this.index = 0; 
  }

  public boolean hasNext() {
    return index < priceArray.length;
  }
  
  public GoodPrice next() {
    return priceArray[index++];
  }
  
  public void remove() {
    throw new UnsupportedOperationException();
  }
  
  public Iterator<GoodPrice> iterator() {
    return this;
  }

}
