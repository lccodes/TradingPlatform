package generatePredictions;

import java.util.Hashtable;

import brown.interfaces.IValuation;


public abstract class SubRandomVal implements IValuation {
private Hashtable<String, Double> _ht;

  public SubRandomVal(){
    _ht = new Hashtable<String, Double>();
    this.populate();
  }
  
  public void populate(){
    String goodA = new String();
    String goodB= new String();
    _ht.put(goodA, 1.5);
    _ht.put(goodB, 2.3);
  }
  
  @Override
  public double getValuation(String s) {
    return _ht.get(s);
    
    
  }
  
}