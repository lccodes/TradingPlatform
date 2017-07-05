package brown.valuation; 

import java.util.Iterator;

public class ValuationIterator implements Iterable<Valuation>, Iterator<Valuation>{
  
  private Valuation[] valArray;
  private int index;
  
  public ValuationIterator(ValuationBundle valSet) {
    this.valArray = valSet.toArray(); 
    this.index = 0; 
  }

  public boolean hasNext() {
    return index < valArray.length;
  }
  
  public Valuation next() {
    return valArray[index++];
  }
  
  public void remove() {
    throw new UnsupportedOperationException();
  }
  
  public Iterator<Valuation> iterator() {
    return this;
  }

}
