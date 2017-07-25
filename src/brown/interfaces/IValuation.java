
package brown.interfaces;

import java.util.Set;

public interface IValuation {

  double getValuation(Good g);
  double getValuation(Set<Good> goods);

}