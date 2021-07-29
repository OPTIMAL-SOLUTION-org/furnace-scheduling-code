/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model.constants.sub;

import java.util.Map;

public interface IConstants_s_ijk {

  Map<Integer, Map<Integer, Map<Integer, Double>>> get_s_ijk();

  default double get_s_ijk(
      int i,
      int j,
      int k) {
    return get_s_ijk().get(i).get(j).get(k);
  }
}
