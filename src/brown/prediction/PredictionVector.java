package brown.prediction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import brown.assets.value.FullType;

public class PredictionVector implements Iterable<GoodPrice> {
  
  private Map<FullType, Double> priceMap;
  
  public PredictionVector() {
    this.priceMap = new HashMap<FullType, Double>();
  }
  
  
  public PredictionVector (PredictionVector p) {
    this.priceMap = new HashMap<FullType, Double>();
    this.addAll(p);
  }
  
  public PredictionVector(Map<FullType, Double> aMap) {
    this.priceMap = new HashMap<FullType, Double>(aMap);
    }
  
  public void add(GoodPrice val) {
    priceMap.put(val.getGood(), val.getPrice());
  }
  
  public void add(FullType good, Double price) {
    priceMap.put(good, price);
  }
  
  public void clear() {
    priceMap = new HashMap<FullType, Double>();
  }
  
  public Boolean contains(FullType good) {
    return priceMap.containsKey(good);
  }
  
  public Boolean contains(GoodPrice aGood) {
    return priceMap.containsKey(aGood.getGood());
  }
  
  public GoodPrice getGoodPrice(FullType good) {
    return new GoodPrice(good, priceMap.get(good));
  }
  
  public Double getOrDefault(FullType good, Double defVal) {
    if(priceMap.containsKey(good)) {
      return priceMap.get(good);
    }
    else {
      return defVal;
    }
  }
  
  public Boolean isEmpty() {
    return (priceMap.isEmpty());
  }
  
  public void addAll(Map<FullType, Double> goods) {
    priceMap.putAll(goods);
  }
  
  public void addAll(PredictionVector predictions) {
    for(GoodPrice p : predictions) {
      this.add(p);
    }
  }
  
  public void remove(GoodPrice good) {
    priceMap.remove(good.getGood());
  }
  
  public Integer size() {
    return priceMap.size();
  }
  
  public GoodPrice[] toArray() {
    GoodPrice[] priceArray = new GoodPrice[this.size()];
    int i = 0; 
    for(FullType good : priceMap.keySet()) {
      priceArray[i] = this.getGoodPrice(good);
      i++;
    }
    return priceArray;
  }
  
  public Iterator<GoodPrice> iterator() {
    GoodPriceIterator v = new GoodPriceIterator(this);
    return v;
}

  @Override
  public String toString() {
    return "PredictionVector [priceMap=" + priceMap + "]";
  }
  

  
  
}
