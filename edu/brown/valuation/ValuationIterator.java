package brown.valuation; 

import java.util.Iterator;

/**
 * Iterator for ValuationBundle class. is currently read-only, meaning that it
 * creates an array copy and iterates over that. 
 * @author acoggins
 *
 */
public class ValuationIterator implements Iterable<Valuation>, Iterator<Valuation>{
  
  private Valuation[] valArray;
  private int index;
  
  /**
   * Valuation iterator constructor.
   * @param valSet
   * a ValuationBundle to be iterated over. 
   */
  public ValuationIterator(ValuationBundle valSet) {
    this.valArray = valSet.toArray(); 
    this.index = 0; 
  }

  /**
   * determines whether the value array has a next element. 
   */
  public boolean hasNext() {
    return index < valArray.length;
  }
  
  /**
   * returns the next element in valArray.
   */
  public Valuation next() {
    return valArray[index++];
  }
  
  /**
   * throws an exception.(?)
   */
  public void remove() {
    throw new UnsupportedOperationException();
  }
  
  /**
   * calls the valuation iterator for use on ValuationBundle.
   */
  public Iterator<Valuation> iterator() {
    return this;
  }

}
