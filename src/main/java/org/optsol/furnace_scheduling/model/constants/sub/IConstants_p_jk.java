/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model.constants.sub;

import java.util.Map;

public interface IConstants_p_jk {

  Map<Integer, Map<Integer, Double>> get_p_jk();

  default double get_p_jk(
      int j,
      int k) {
    return get_p_jk().get(j).get(k);
  }
}
