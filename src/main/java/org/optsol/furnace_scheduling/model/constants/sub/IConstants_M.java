/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model.constants.sub;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface IConstants_M {
  // num of machines
  int get_m();

  // set of machines M
  default List<Integer> get_M() {
    return IntStream.rangeClosed(1, get_m()).boxed().collect(Collectors.toList());
  }
}
