/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model.constants.sub;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface IConstants_J {
  // num of jobs
  int get_n();

  // set of jobs J
  default List<Integer> get_J() {
    return IntStream.rangeClosed(1, get_n()).boxed().collect(Collectors.toList());
  }

  // set of jobs J and Start-DummyJob
  default List<Integer> get_J0() {
    return IntStream.rangeClosed(0, get_n()).boxed().collect(Collectors.toList());
  }
}
