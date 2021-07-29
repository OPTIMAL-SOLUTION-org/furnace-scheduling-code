/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model.constants.sub;

import java.util.Map;
import java.util.Set;

public interface IConstants_M_j {

  Map<Integer, Set<Integer>> get_M_j();

  default Set<Integer> get_M_j(int j) {
    return get_M_j().get(j);
  }
}
